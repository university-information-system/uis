package at.ac.tuwien.inso.service;

import at.ac.tuwien.inso.entity.*;

public interface UserCreationService {

    PendingAccountActivation create(UisUser user);
}
