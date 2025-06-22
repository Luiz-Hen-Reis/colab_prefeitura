package com.henr.colab_prefeitura.contracts;

import com.henr.colab_prefeitura.modules.users.entities.User;

public interface AuthenticatedUserProviderContract {
    User getAuthenticatedUser();
}
