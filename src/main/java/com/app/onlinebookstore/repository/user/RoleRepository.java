package com.app.onlinebookstore.repository.user;

import com.app.onlinebookstore.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role getRoleByName(Role.RoleName roleName);
}
