package com.example.fitpassserver.owner.owner.service.command;

import com.example.fitpassserver.owner.owner.dto.OwnerRequestDTO;
import com.example.fitpassserver.owner.owner.entity.Owner;

public interface OwnerCommandService {
    Owner joinOwner(OwnerRequestDTO.OwnerJoinDTO request);
}
