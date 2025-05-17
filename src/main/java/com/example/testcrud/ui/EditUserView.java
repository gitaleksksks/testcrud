package com.example.testcrud.ui;

import com.example.testcrud.dto.UserDTO;
import com.example.testcrud.exception.ResourceNotFoundException;
import com.example.testcrud.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Route("edit-user")
public class EditUserView extends FormLayout implements BeforeEnterObserver {

    private final UserService userService;

    private final TextField lastName = new TextField("Фамилия");
    private final TextField firstName = new TextField("Имя");
    private final TextField email = new TextField("Email");

    private Long userId;

    @Autowired
    public EditUserView(UserService userService) {
        this.userService = userService;

        Button updateButton = new Button("Обновить", event -> {
            try {
                UserDTO userDTO = new UserDTO();
                userDTO.setId(userId);
                userDTO.setLastName(lastName.getValue());
                userDTO.setFirstName(firstName.getValue());
                userDTO.setEmail(email.getValue());
                userService.updateUser(userId, userDTO);
                Notification.show("Пользователь успешно обновлен");
                getUI().ifPresent(ui -> ui.navigate(MainView.class));
            } catch (ResourceNotFoundException e) {
                Notification.show(e.getMessage());
            }
        });

        add(lastName, firstName, email, updateButton);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        userId = Long.valueOf(event.getRouteParameters().get("id").orElseThrow());
        Optional<UserDTO> user = userService.getUserById(userId);
        lastName.setValue(user.get().getLastName());
        firstName.setValue(user.get().getFirstName());
        email.setValue(user.get().getEmail());
    }
}
