package br.com.bibliotec.ui.componets;

import com.vaadin.flow.component.datepicker.DatePicker;

import java.io.Serial;
import java.util.List;

public class DatePickerPT extends DatePicker {
    
    @Serial
    private static final long serialVersionUID = 5201333412700891178L;
    
    public DatePickerPT(String label) {
        super(label);
        
        defineI18n();
    }
    
    private void defineI18n() {
        DatePickerI18n ptBrI18n = new DatePickerI18n();
        ptBrI18n.setMonthNames(List.of("Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"));
        ptBrI18n.setWeekdays(List.of("Domingo", "Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado"));
        ptBrI18n.setWeekdaysShort(List.of("Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sáb"));
        ptBrI18n.setToday("Hoje");
        ptBrI18n.setCancel("Cancelar");
        setI18n(ptBrI18n);
    }
}
