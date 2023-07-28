package shop.jbshop.controller;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.Banner;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import shop.jbshop.dto.request.*;
import shop.jbshop.dto.response.AllItemResponseDto;
import shop.jbshop.dto.response.MemberResponseDto;
import shop.jbshop.service.ItemService;
import shop.jbshop.service.KakaoService;
import shop.jbshop.service.MemberService;

import java.nio.channels.ScatteringByteChannel;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final KakaoService kakaoService;
    private final MemberService memberService;
    private final ItemService itemService;

    @GetMapping("test")
    public String test() {
        return "main1";
    }


    @GetMapping("/")
    public String home(HttpSession session,
                       Model model,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "9") int size) {
        session.invalidate();
        Page<AllItemResponseDto> itemsPage = itemService.findItems(PageRequest.of(page, size));
        model.addAttribute("items", itemsPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalPages", itemsPage.getTotalPages());
        return "main";
    }


    // 일반 ITEM 뿌리기
    @GetMapping("/main")
    public String home(Model model,
                       HttpSession session,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "9") int size) {
        Long memberId = (Long) session.getAttribute("memberId");
        Long cartCount = memberService.findCartCount(memberId);
        Page<AllItemResponseDto> itemsPage = itemService.findItems(PageRequest.of(page, size));
        model.addAttribute("items", itemsPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalPages", itemsPage.getTotalPages());
        model.addAttribute("cartCount", cartCount);
        String message = (String) session.getAttribute("message");
        if (message != null) {
            model.addAttribute("message", message);
            session.removeAttribute("message");
        }
        return "main";
    }

    // 카테고리별 ITEM 뿌리기
    @GetMapping("/main/{category}")
    public String categoryHome(@PathVariable String category,
                               HttpSession session,
                               Model model,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "9") int size) {
        System.out.println("cate로온다" + category);
        Long memberId = (Long) session.getAttribute("memberId");
        Long cartCount = memberService.findCartCount(memberId);
        Page<AllItemResponseDto> itemsPage = itemService.findItems(PageRequest.of(page, size), category);
        int totalPages = itemsPage.getTotalPages() == 0 ? 1 : itemsPage.getTotalPages();
        model.addAttribute("items", itemsPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("cartCount", cartCount);
        String message = (String) session.getAttribute("message");
        if (message != null) {
            model.addAttribute("message", message);
            session.removeAttribute("message");
        }
        return "/item/category";
    }

    @GetMapping("/text")
    public String searchHome(@RequestParam(name = "searchText") String searchText,
                             HttpSession session,
                             Model model,
                             @RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "9") int size) {
        System.out.println("text로온다" + searchText);
        Long memberId = (Long) session.getAttribute("memberId");
        Long cartCount = memberService.findCartCount(memberId);

        // 카테고리와 검색어로 상품을 검색하고 페이지를 구성합니다.
        Page<AllItemResponseDto> itemsPage = itemService.searchItems(PageRequest.of(page, size), searchText);

        int totalPages = itemsPage.getTotalPages() == 0 ? 1 : itemsPage.getTotalPages();
        model.addAttribute("items", itemsPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("cartCount", cartCount);
        model.addAttribute("searchText", searchText); // 검색어를 뷰로 전달합니다.

        String message = (String) session.getAttribute("message");
        if (message != null) {
            model.addAttribute("message", message);
            session.removeAttribute("message");
        }
        return "/item/search";
    }


    @GetMapping("/join")
    public String join() {
        return "join";
    }

    @PostMapping("/join")
    public String join(@Validated MemberDirectCreateRequestDto dto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("dto", dto);
            return "join";
        }
        try {
            Long memberId = memberService.joinMember(dto);
            return "redirect:login";
        } catch (DuplicateKeyException e) {
            throw e;
        }
    }


    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@Validated MemberLoginRequestDto dto, HttpSession session) {
        MemberResponseDto findMember = memberService.findMemberByEmail(dto.getEmail());

        if (findMember != null) {
            if (findMember.getPassword().equals(dto.getPassword())) {
                session.setAttribute("memberId", findMember.getId());
                return "redirect:main";
            } else {
                return "redirect:login";
            }
        } else {
            return "redirect:login";
        }
    }

    //kakao login
    @GetMapping("/kakao/login")
    public String getKakaoAccount(@RequestParam("code") String code, HttpSession session) throws Exception {
        String accessToken = kakaoService.getAccessToken(code);
        KakaoDto dto = kakaoService.getUserInfo(accessToken);
        MemberResponseDto findMember = memberService.findMemberByEmail(dto.getEmail());

        if (findMember != null) {
            // 이미 가입된 회원이라면 세션에 회원 ID를 저장하고 메인 페이지로 이동합니다.
            session.setAttribute("memberId", findMember.getId());
            return "redirect:/main";
        } else {
            // 가입되지 않은 회원이라면 회원가입 페이지로 이동합니다.
            Long memberId = memberService.joinMemberByOauth(dto);
            session.setAttribute("memberId", memberId);
            return "redirect:/main";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/main";
    }

    @GetMapping("/memberDetail")
    public String memberDetail(HttpSession session, Model model) {

        Long memberId = (Long) session.getAttribute("memberId");
        MemberResponseDto dto = memberService.findMember(memberId);
        model.addAttribute("member", dto);

        return "/member/memberDetail";
    }

    @GetMapping("/memberUpdate")
    public String memberUpdate(HttpSession session, Model model) {

        Long memberId = (Long) session.getAttribute("memberId");
        MemberResponseDto dto = memberService.findMember(memberId);
        model.addAttribute("member", dto);
        return "/member/memberUpdate";
    }

    @PostMapping("/memberUpdate")
    public String memberUpdate(HttpSession session, MemberUpdateRequestDto updateDto, Model model) {
        Long memberId = (Long) session.getAttribute("memberId");
        MemberResponseDto findDto = memberService.findMember(memberId);

        memberService.updateMember(findDto.getEmail(), updateDto);
        MemberResponseDto dto = memberService.findMember(memberId);
        model.addAttribute("member", dto);
        return "/member/memberDetail";
    }


    @GetMapping("/confirmDelete")
    public String confirmDelete(HttpSession session, Model model) {

        Long memberId = (Long) session.getAttribute("memberId");
        MemberResponseDto dto = memberService.findMember(memberId);
        model.addAttribute("member", dto);
        return "/member/confirmDelete";
    }

    @PostMapping("/deleteMember")
    public String deleteMember(HttpSession session, Model model, MemberDeleteRequestDto dto) {
        Long memberId = (Long) session.getAttribute("memberId");
        MemberResponseDto findMember = memberService.findMember(memberId);

        if (dto.getPassword().equals(findMember.getPassword())) {
            memberService.deleteMember(findMember.getEmail());
            session.invalidate(); // 세션 무효화
            model.addAttribute("message", "삭제되었습니다");
            return "/member/memberDeleted";
        } else {
            model.addAttribute("message", "비밀번호가 틀립니다");
            return "redirect:/member/memberDetail";
        }
    }




}
