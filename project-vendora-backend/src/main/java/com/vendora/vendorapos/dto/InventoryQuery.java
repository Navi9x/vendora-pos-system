package com.vendora.vendorapos.dto;

import com.vendora.vendorapos.entity.enums.StockStatus;
import com.vendora.vendorapos.entity.enums.UnitType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class InventoryQuery {
    @Min(0)
    private Integer page;

    @Min(0) @Max(50)
    private Integer size;

    private String searchText;
    private Long categoryId;
    // null = not provided, true/false = filter
    private Boolean active;
    private StockStatus stockStatus;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;
}
