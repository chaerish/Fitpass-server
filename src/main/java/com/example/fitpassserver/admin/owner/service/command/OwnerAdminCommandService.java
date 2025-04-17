package com.example.fitpassserver.admin.owner.service.command;

public interface OwnerAdminCommandService {
    void patchOwnerStatus(String loginId, Boolean isApproval);
}
