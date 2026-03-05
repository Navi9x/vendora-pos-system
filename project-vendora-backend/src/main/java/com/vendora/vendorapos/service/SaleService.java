package com.vendora.vendorapos.service;

import com.vendora.vendorapos.dto.CreateSaleDTO;
import com.vendora.vendorapos.dto.SaleItemRequestDTO;
import com.vendora.vendorapos.entity.*;
import com.vendora.vendorapos.entity.enums.SaleStatus;
import com.vendora.vendorapos.repo.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SaleService {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private SaleRepo saleRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private BusinessRepo businessRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private SaleItemRepo saleItemRepo;

    @Autowired
    private CustomerRepo customerRepo;

    public int getTotalSales(Long tenantId) {
        return saleRepo.countByBusiness_Id(tenantId);
    }

    @Transactional
    public void saveSale(CreateSaleDTO createSaleDTO, Long tenantId, String userEmail) {

        User user = userRepo.findByEmail(userEmail);
        if (user == null) {
            throw new RuntimeException("User not found: " + userEmail);
        }
        Business business = businessRepo.findById(tenantId).
                orElseThrow(() -> new RuntimeException("Business not found: " + tenantId));;

        List<SaleItemRequestDTO> itemRequestDTOs = createSaleDTO.getItems();
        if (itemRequestDTOs == null||itemRequestDTOs.isEmpty()) {
            throw new RuntimeException("Sale must contain at least 1 item");
        }

        BigDecimal subtotal = BigDecimal.ZERO;
        for (SaleItemRequestDTO item : itemRequestDTOs) {
            if (item.getQuantity() == null || item.getQuantity() <= 0) {
                throw new RuntimeException("Invalid quantity for productId: " + item.getId());
            }
            if (item.getPrice() == null) {
                throw new RuntimeException("Missing price for productId: " + item.getId());
            }
            BigDecimal subtotalVal = item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            subtotal = subtotal.add(subtotalVal);
        }

        subtotal = subtotal.setScale(2, RoundingMode.HALF_UP);

        BigDecimal delivery = createSaleDTO.getDeliveryCharge() != null ? createSaleDTO.getDeliveryCharge() : BigDecimal.ZERO;
        BigDecimal tax      = createSaleDTO.getTaxAmount()      != null ? createSaleDTO.getTaxAmount()      : BigDecimal.ZERO;
        BigDecimal discount = createSaleDTO.getDiscount()       != null ? createSaleDTO.getDiscount()       : BigDecimal.ZERO;

        BigDecimal total = subtotal
                .add(delivery)
                .add(tax)
                .subtract(discount)
                .setScale(2, RoundingMode.HALF_UP);

        if (total.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Total cannot be negative");
        }

        Sale sale = new Sale();

        //Get customer
        if(createSaleDTO.getCustomerId()!=0){
            Customer customer = customerRepo.findById(createSaleDTO.getCustomerId()).orElse(null);
            sale.setCustomer(customer);
        }

        sale.setInvoiceNumber(createSaleDTO.getInvoiceNumber());
        sale.setCashier(user);
        sale.setNotes(createSaleDTO.getNotes());
        sale.setStatus(SaleStatus.COMPLETED);

        sale.setDiscount(discount);
        sale.setTaxAmount(tax);
        sale.setDeliveryCharge(delivery);

        sale.setBusiness(business);
        sale.setPaymentStatus(createSaleDTO.getPaymentStatus());
        sale.setSubtotal(subtotal);
        sale.setTotal(total);
        sale.setCreatedAt(LocalDateTime.now());

        Sale savedSale = saleRepo.save(sale);

        for (SaleItemRequestDTO item : itemRequestDTOs) {
            SaleItem saleItem = new SaleItem();
            saleItem.setSale(savedSale);
            Product product = productRepo.findByIdAndBusiness_id(item.getId(),tenantId);
            saleItem.setProduct(product);
            saleItem.setQuantity(item.getQuantity());
            saleItemRepo.save(saleItem);

            inventoryService.reduceStock(tenantId, product.getId(), item.getQuantity());

        }

    }
}
