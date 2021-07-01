package com.example.demo.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.models.InquiryForm;

import com.example.demo.models.ItemForm;
import com.example.demo.repositries.InquiryRepository;

import com.example.demo.repositries.ItemRepository;


@Controller
@RequestMapping("/")
public class RootController {

	@Autowired
	InquiryRepository repository;
	
	@Autowired
	ItemRepository itemRepository;
	

	@GetMapping
	public String index() {
		return "root/index";
	}

	@GetMapping("/form")
	public String form(InquiryForm inquiryForm) {
		return "root/form";
	}
	
	
	@GetMapping("/item/create")
	public String create(ItemForm itemForm) {
		return "root/item/create";
	}
	
	@GetMapping("/item/list")
	public String list(Model model) {
		List<ItemForm> itemForm = itemRepository.findAll();
		model.addAttribute("itemForm", itemForm);

		return "root/item/list";
	}
	
	@RequestMapping("/item/edit")
    public String edit(Model model, @RequestParam("select") String select) {
		Optional<ItemForm> item = itemRepository.findById(Long.parseLong(select));
		model.addAttribute("itemForm", item);   
        return "root/item/edit";
    }
	
	@RequestMapping("/item/deleteConfirm")
    public String deleteConfirm(Model model, @RequestParam("select") String select) {
		Optional<ItemForm> item = itemRepository.findById(Long.parseLong(select));		
		model.addAttribute("itemForm", item);   
       return "/root/item/deleteConfirm";
    }


	@PostMapping("/form")
	public String form(@Validated InquiryForm inquiryForm, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			return "root/form";
		}

		// RDBと連携できることを確認しておきます。
		repository.saveAndFlush(inquiryForm);
		inquiryForm.clear();
		model.addAttribute("message", "お問い合わせを受け付けました。");
		return "root/form";
	}
	
	@PostMapping("/item/create")
	public String create(@Validated ItemForm itemForm, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			return "root/item/create";
		}

		// RDBと連携できることを確認しておきます。
		itemRepository.saveAndFlush(itemForm);
		itemForm.clear();
		model.addAttribute("message", "商品の追加を受け付けました。");
		return "root/item/create";
	}
	
	@PostMapping("/item/edit")
	  String update(@Validated ItemForm itemForm, BindingResult bindingResult, Model model) {
	    if (bindingResult.hasErrors()) {
	      return "root/item/edit";
	    }
	    itemRepository.saveAndFlush(itemForm);
	   
		model.addAttribute("message", "商品の編集を受け付けました。");
	    return "root/item/edit";
	  }
	
	@PostMapping("/item/delete")
	  String delete(@RequestParam(name = "id", required = false) long id,@Validated ItemForm itemForm,  Model model) {
	    itemRepository.deleteById(id);
	   
	    return "root/item/delete";
	  }
	
	
}