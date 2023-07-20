package shop.jbshop.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import shop.jbshop.dto.request.MemberLoginRequestDto;
import shop.jbshop.dto.request.MemberDeleteRequestDto;
import shop.jbshop.dto.request.MemberDirectCreateRequestDto;
import shop.jbshop.dto.request.MemberUpdateRequestDto;
import shop.jbshop.dto.response.AllItemResponseDto;
import shop.jbshop.dto.response.MemberResponseDto;
import shop.jbshop.service.ItemService;
import shop.jbshop.service.MemberService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final MemberService memberService;
    private final ItemService itemService;

    @GetMapping("test")
    public String test() {
        return "main1";
    }


    @GetMapping("/")
    public String home1(HttpSession session, Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        session.invalidate();
        Page<AllItemResponseDto> itemsPage = itemService.findItems(PageRequest.of(page, size));
        model.addAttribute("items", itemsPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalPages", itemsPage.getTotalPages());
        return "main";
    }


    @GetMapping("/main")
    public String home2(Model model, HttpSession session, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "9") int size) {
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

    //카테고리별 이동
    @GetMapping("/main/{category}")
    public String categorySearch(@PathVariable String category,
                                 HttpSession session,
                                 Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "9") int size) {
        Long memberId = (Long) session.getAttribute("memberId");
        Long cartCount = memberService.findCartCount(memberId);
        Page<AllItemResponseDto> itemsPage = itemService.findItems(PageRequest.of(page, size), category);
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
        return "/main";
    }


    @GetMapping("/join")
    public String join() {
        return "join";
    }

    @PostMapping("/join")
    public String join(@Validated MemberDirectCreateRequestDto dto) {
        memberService.joinMember(dto);
        return "redirect:main";
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