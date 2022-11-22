package com.rabbit.processor.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Данный контейнер будет хранить результат обработки
 * сообщения полученного из канала
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccumulatorMessage {
    int currentValue;
    int total;
}
