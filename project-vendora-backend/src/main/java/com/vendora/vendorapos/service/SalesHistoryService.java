package com.vendora.vendorapos.service;

import com.vendora.vendorapos.dto.*;
import com.vendora.vendorapos.entity.*;
import com.vendora.vendorapos.entity.enums.PaymentStatus;
import com.vendora.vendorapos.entity.enums.SaleStatus;
import com.vendora.vendorapos.entity.enums.UserRole;
import com.vendora.vendorapos.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SalesHistoryService {
    @Autowired
    private SaleRepo saleRepo;
    @Autowired
    private SaleItemRepo saleItemRepo;
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private InventoryRepo inventoryRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private CustomerRepo customerRepo;
    @Autowired
    private BusinessRepo businessRepo;

    public List<SaleResponseDTO> loadSalesHistory(Long tenantId, int page, int size, LocalDate date, Long cashierId, SaleStatus saleStatus, PaymentStatus paymentStatus) {
        if(date != null){
            date= date.plusDays(1);
        }
        Page<Sale> sales = saleRepo.searchSales(tenantId, cashierId,date,saleStatus,paymentStatus,PageRequest.of(page,size));

        return salePageToSaleResponseDTO(sales);
    }

    public List<SaleResponseDTO> loadAllSalesHistory(Long tenantId,String date) {
        LocalDate today = LocalDate.now();
        LocalDate startDate = null;
        LocalDate endDate = null;

        switch (date) {
            case "today":
                startDate = today;
                endDate = today;
                break;

            case "yesterday":
                startDate = today.minusDays(1);
                endDate = today.minusDays(1);
                break;

            case "this_week":
                startDate = today.with(DayOfWeek.MONDAY);
                endDate = today;
                break;

            case "this_month":
                startDate = today.withDayOfMonth(1);
                endDate = today;
                break;

            case "last_month":
                LocalDate lastMonth = today.minusMonths(1);
                startDate = lastMonth.withDayOfMonth(1);
                endDate = lastMonth.withDayOfMonth(lastMonth.lengthOfMonth());
                break;

            default:
                throw new IllegalArgumentException("Invalid date filter: " + date);
        }
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(LocalTime.MAX);
        return saleListToSaleResponseDTO(saleRepo.findAllByBusinessAndCreatedAtBetween((businessRepo.findById(tenantId).get()),
                start,
                end));
    }
    public List<SalesChartResponseDTO> loadSalesChart(Long tenantId, String period) {

            LocalDate today = LocalDate.now();
            LocalDate startDate;
            LocalDate endDate;

            switch (period) {
                case "today":
                    startDate = today;
                    endDate = today;
                    break;

                case "yesterday":
                    startDate = today.minusDays(1);
                    endDate = today.minusDays(1);
                    break;

                case "this_week":
                    startDate = today.with(java.time.DayOfWeek.MONDAY);
                    endDate = today;
                    break;

                case "this_month":
                    startDate = today.withDayOfMonth(1);
                    endDate = today;
                    break;

                case "last_month":
                    LocalDate lastMonth = today.minusMonths(1);
                    startDate = lastMonth.withDayOfMonth(1);
                    endDate = lastMonth.withDayOfMonth(lastMonth.lengthOfMonth());
                    break;

                default:
                    throw new IllegalArgumentException("Invalid period: " + period);
            }

            LocalDateTime startDateTime = startDate.atStartOfDay();
            LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

            List<Sale> sales = saleRepo.findAllByBusinessAndCreatedAtBetween(
                    businessRepo.findById(tenantId).get(),
                    startDateTime,
                    endDateTime
            );

            Map<LocalDate, List<Sale>> groupedSales = sales.stream()
                    .collect(Collectors.groupingBy(s -> s.getCreatedAt().toLocalDate(), TreeMap::new, Collectors.toList()));

            List<SalesChartResponseDTO> chartData = new ArrayList<>();

            LocalDate current = startDate;
            while (!current.isAfter(endDate)) {
                List<Sale> dailySales = groupedSales.getOrDefault(current, Collections.emptyList());

                BigDecimal revenue = dailySales.stream()
                        .map(s -> s.getTotal() != null ? s.getTotal() : BigDecimal.ZERO)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                long transactions = dailySales.size();

                SalesChartResponseDTO dto = new SalesChartResponseDTO();
                dto.setLabel(current.toString());
                dto.setRevenue(revenue);
                dto.setTransactions(transactions);

                chartData.add(dto);
                current = current.plusDays(1);
            }

            return chartData;

    }

    public List<SaleResponseDTO> salePageToSaleResponseDTO(Page<Sale> sales) {
        List<SaleResponseDTO> salesHistory = new ArrayList<>();
        for (Sale sale : sales) {
            SaleResponseDTO saleResponseDTO = new SaleResponseDTO();
            saleResponseDTO.setId(sale.getId());
            saleResponseDTO.setCreatedAt(sale.getCreatedAt());
            saleResponseDTO.setUpdatedAt(sale.getUpdatedAt());
            saleResponseDTO.setIsDeleted(sale.getIsDeleted());

            saleResponseDTO.setInvoiceNumber(sale.getInvoiceNumber());
            saleResponseDTO.setSubtotal(sale.getSubtotal());
            saleResponseDTO.setTaxAmount(sale.getTaxAmount());
            saleResponseDTO.setDeliveryCharge(sale.getDeliveryCharge());
            saleResponseDTO.setDiscount(sale.getDiscount());
            saleResponseDTO.setTotal(sale.getTotal());
            saleResponseDTO.setStatus(sale.getStatus());
            saleResponseDTO.setPaymentStatus(sale.getPaymentStatus());
            saleResponseDTO.setNotes(sale.getNotes());

            User user = sale.getCashier();
            String roleString = user.getRole().toString();
            UserDTO userDTO = new UserDTO(
                    user.getId(),
                    user.getEmail(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getPhone(),
                    roleString,
                    user.getIsActive(),
                    null,
                    null
            );
            saleResponseDTO.setCashier(userDTO);

            if(sale.getCustomer() != null) {
                Customer customer = sale.getCustomer();
                CustomerDTO customerDTO = new CustomerDTO(
                        customer.getId(),
                        customer.getName(),
                        customer.getPhone()
                );
                saleResponseDTO.setCustomer(customerDTO);
            }
            Business business = sale.getBusiness();
            BusinessDTO businessDTO = new BusinessDTO(
                    business.getId(),
                    business.getBusinessName(),
                    business.getBusinessCode(),
                    business.getSystemType().toString(),
                    business.getSubscriptionPlan().toString(),
                    business.getIsActive(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    business.getCreatedAt()
            );

            saleResponseDTO.setBusiness(businessDTO);
            List<SaleItem> saleItems = saleItemRepo.findAllBySale(sale);
            List<SaleItemResponseDTO> itemResponseDTOS = new ArrayList<>();
            int totalQuantity = 0;
            for (SaleItem saleItem : saleItems) {
                Product product = saleItem.getProduct();
                double total = product.getPrice()*saleItem.getQuantity();
                SaleItemResponseDTO saleItemResponseDTO = new SaleItemResponseDTO(
                        saleItem.getId(),
                        saleItem.getQuantity(),
                        new ProductDTO(
                                product.getId(),
                                product.getName(),
                                product.getPrice(),
                                product.getCategory().getName(),
                                product.getImage(),
                                product.getIsActive()
                        ),
                        BigDecimal.valueOf(product.getPrice()),
                        BigDecimal.valueOf(total)
                );
                totalQuantity += saleItem.getQuantity();
                itemResponseDTOS.add(saleItemResponseDTO);
            }
            saleResponseDTO.setItems(itemResponseDTOS);

            saleResponseDTO.setTotalItems(saleItems.size());
            saleResponseDTO.setTotalQuantity(totalQuantity);
            salesHistory.add(saleResponseDTO);
        }
        return salesHistory;
    }
    public List<SaleResponseDTO> saleListToSaleResponseDTO(List<Sale> sales) {
        List<SaleResponseDTO> salesHistory = new ArrayList<>();
        for (Sale sale : sales) {
            SaleResponseDTO saleResponseDTO = new SaleResponseDTO();
            saleResponseDTO.setId(sale.getId());
            saleResponseDTO.setCreatedAt(sale.getCreatedAt());
            saleResponseDTO.setUpdatedAt(sale.getUpdatedAt());
            saleResponseDTO.setIsDeleted(sale.getIsDeleted());

            saleResponseDTO.setInvoiceNumber(sale.getInvoiceNumber());
            saleResponseDTO.setSubtotal(sale.getSubtotal());
            saleResponseDTO.setTaxAmount(sale.getTaxAmount());
            saleResponseDTO.setDeliveryCharge(sale.getDeliveryCharge());
            saleResponseDTO.setDiscount(sale.getDiscount());
            saleResponseDTO.setTotal(sale.getTotal());
            saleResponseDTO.setStatus(sale.getStatus());
            saleResponseDTO.setPaymentStatus(sale.getPaymentStatus());
            saleResponseDTO.setNotes(sale.getNotes());

            User user = sale.getCashier();
            String roleString = user.getRole().toString();
            UserDTO userDTO = new UserDTO(
                    user.getId(),
                    user.getEmail(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getPhone(),
                    roleString,
                    user.getIsActive(),
                    null,
                    null
            );
            saleResponseDTO.setCashier(userDTO);

            if(sale.getCustomer() != null) {
                Customer customer = sale.getCustomer();
                CustomerDTO customerDTO = new CustomerDTO(
                        customer.getId(),
                        customer.getName(),
                        customer.getPhone()
                );
                saleResponseDTO.setCustomer(customerDTO);
            }
            Business business = sale.getBusiness();
            BusinessDTO businessDTO = new BusinessDTO(
                    business.getId(),
                    business.getBusinessName(),
                    business.getBusinessCode(),
                    business.getSystemType().toString(),
                    business.getSubscriptionPlan().toString(),
                    business.getIsActive(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    business.getCreatedAt()
            );

            saleResponseDTO.setBusiness(businessDTO);
            List<SaleItem> saleItems = saleItemRepo.findAllBySale(sale);
            List<SaleItemResponseDTO> itemResponseDTOS = new ArrayList<>();
            int totalQuantity = 0;
            for (SaleItem saleItem : saleItems) {
                Product product = saleItem.getProduct();
                double total = product.getPrice()*saleItem.getQuantity();
                SaleItemResponseDTO saleItemResponseDTO = new SaleItemResponseDTO(
                        saleItem.getId(),
                        saleItem.getQuantity(),
                        new ProductDTO(
                                product.getId(),
                                product.getName(),
                                product.getPrice(),
                                product.getCategory().getName(),
                                product.getImage(),
                                product.getIsActive()
                        ),
                        BigDecimal.valueOf(product.getPrice()),
                        BigDecimal.valueOf(total)
                );
                totalQuantity += saleItem.getQuantity();
                itemResponseDTOS.add(saleItemResponseDTO);
            }
            saleResponseDTO.setItems(itemResponseDTOS);

            saleResponseDTO.setTotalItems(saleItems.size());
            saleResponseDTO.setTotalQuantity(totalQuantity);
            salesHistory.add(saleResponseDTO);
        }
        return salesHistory;
    }

}
