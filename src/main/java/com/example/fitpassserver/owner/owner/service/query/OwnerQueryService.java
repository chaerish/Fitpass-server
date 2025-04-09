package com.example.fitpassserver.owner.owner.service.query;


import com.example.fitpassserver.owner.owner.entity.Owner;

public interface OwnerQueryService {
    Owner getOwner(String loginId);
}
