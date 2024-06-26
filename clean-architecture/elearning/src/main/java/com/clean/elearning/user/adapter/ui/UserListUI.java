package com.clean.elearning.user.adapter.ui;

import com.clean.elearning.user.adapter.dto.UpdateUserRequest;
import com.clean.elearning.user.adapter.ui.model.UserViewModel;

import java.util.List;

public interface UserListUI {

    void showUsers(List<UserViewModel> users);

    void navigateToCreateUserFormView();

    void navigateToEditUserFormView(UpdateUserRequest updateUserRequest);

    void showDeleteUserConfirmDialog(UserViewModel user);

}
