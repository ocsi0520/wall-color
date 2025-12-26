package com.my_wall_color.color_manager.color.domain;

import com.my_wall_color.color_manager.shared.sorting_and_pagination.domain.FieldProvider;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ColorField implements FieldProvider {
    ID("id"), NAME("name");
    private final String fieldName;
    
    @Override
    public String getFieldName() {
        return this.fieldName;
    }
}
