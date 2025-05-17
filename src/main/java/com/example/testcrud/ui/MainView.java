package com.example.testcrud.ui;

import com.example.testcrud.dto.UserDTO;
import com.example.testcrud.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Route("")
public class MainView extends VerticalLayout {

    private final UserService userService;
    private final Grid<UserDTO> grid = new Grid<>(UserDTO.class);

    @Autowired
    public MainView(UserService userService) {
        this.userService = userService;

        add(new H1("Список пользователей"));

        grid.setColumns("id", "lastName", "firstName", "email");
        grid.setItems(userService.getAllUsers());
        grid.addItemClickListener(event -> {
            Optional<UserDTO> user = Optional.ofNullable(event.getItem());
            getUI().ifPresent(ui -> ui.navigate("edit-user/" + user.get().getId()));
        });
        add(grid);

        Button addUserButton = new Button("Добавить пользователя", event -> {
            getUI().ifPresent(ui -> ui.navigate(AddUserView.class));
        });
        add(addUserButton);
    }
}
