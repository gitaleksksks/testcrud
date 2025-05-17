package com.example.testcrud.ui;

import com.example.testcrud.dto.UserDTO;
import com.example.testcrud.exception.BadRequestException;
import com.example.testcrud.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route("add-user")
public class AddUserView extends FormLayout {

    private final UserService userService;

    private final TextField lastName = new TextField("Фамилия");
    private final TextField firstName = new TextField("Имя");
    private final TextField email = new TextField("Email");

    @Autowired
    public AddUserView(UserService userService) {
        this.userService = userService;

        Button saveButton = new Button("Сохранить", event -> {
            try {
                UserDTO userDTO = new UserDTO();
                userDTO.setLastName(lastName.getValue());
                userDTO.setFirstName(firstName.getValue());
                userDTO.setEmail(email.getValue());
                userService.createUser(userDTO);
                Notification.show("Пользователь успешно добавлен");
                getUI().ifPresent(ui -> ui.navigate(MainView.class));
            } catch (BadRequestException e) {
                Notification.show(e.getMessage());
            }
        });

        add(lastName, firstName, email, saveButton);
    }
}
