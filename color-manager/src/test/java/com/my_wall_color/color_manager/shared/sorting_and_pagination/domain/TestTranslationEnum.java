package com.my_wall_color.color_manager.shared.sorting_and_pagination.domain;

public enum TestTranslationEnum implements FieldProvider {
    ONE("egy"), TWO("kettő"), THREE("három");

    private final String fieldName;

    TestTranslationEnum(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }
}
