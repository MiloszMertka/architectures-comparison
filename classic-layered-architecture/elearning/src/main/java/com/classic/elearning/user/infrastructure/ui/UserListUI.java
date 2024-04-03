package com.classic.elearning.user.infrastructure.ui;

import com.classic.elearning.user.infrastructure.dto.UpdateUserRequest;
import com.classic.elearning.user.infrastructure.ui.model.UserViewModel;

import java.util.List;

public interface UserListUI {

    void showUsers(List<UserViewModel> users);

    void navigateToCreateUserFormView();

    void navigateToEditUserFormView(UpdateUserRequest updateUserRequest);

    void showDeleteUserConfirmDialog(UserViewModel user);

}
