package com.example.fitpassserver.admin.owner.service.command;

import com.example.fitpassserver.owner.owner.entity.OwnerStatus;

public interface OwnerAdminCommandService {
    void patchOwnerStatus(String loginId, OwnerStatus status);
}
