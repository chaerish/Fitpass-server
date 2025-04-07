package com.example.fitpassserver.owner.owner.service.command;

import com.example.fitpassserver.domain.member.exception.MemberErrorCode;
import com.example.fitpassserver.domain.member.exception.MemberException;
import com.example.fitpassserver.domain.member.repository.MemberRepository;
import com.example.fitpassserver.global.aws.s3.service.S3Service;
import com.example.fitpassserver.owner.owner.converter.OwnerConverter;
import com.example.fitpassserver.owner.owner.dto.OwnerRequestDTO;
import com.example.fitpassserver.owner.owner.entity.Owner;
import com.example.fitpassserver.owner.owner.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OwnerCommandServiceImpl implements OwnerCommandService {

    private final OwnerRepository ownerRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3Service s3Service;

    /**
     * 회원가입
     **/
    @Override
    @Transactional
    public Owner joinOwner(OwnerRequestDTO.OwnerJoinDTO request) {
        //중복 아이디 체크
        if (ownerRepository.existsByLoginId(request.getLoginId())) {
            throw new MemberException(MemberErrorCode.DUPLICATE_LOGINID);
        }
        if (memberRepository.existsByLoginId(request.getLoginId())) {
            throw new MemberException(MemberErrorCode.DUPLICATE_LOGINID);
        }
        //이미 가입된 번호인지 확인
//        if (memberRepository.existsByPhoneNumber(request.getPhoneNumber())) {
//            throw new MemberException(MemberErrorCode.DUPLICATE_PHONE_NUMBER);
//        }
//        //인증했는지 확인
//        if (!smsRepository.hasKey(request.getPhoneNumber())) {
//            throw new MemberException(MemberErrorCode.UNVERIFIED_PHONE_NUMBER);
//        }

        //필수 동의 사항 검증
        if (!request.isTermsAgreed() || !request.isPersonalInformationAgreed() || !request.isThirdPartyAgreed()) {
            throw new MemberException(MemberErrorCode.BAD_REQUEST);
        }

        Owner newOwner = OwnerConverter.toOwner(request);

        newOwner.encodePassword(passwordEncoder.encode(request.getPassword()));

        Owner savedOwner = ownerRepository.save(newOwner);

        return savedOwner;
    }

}
