package io.f12.notionlinkedblog.service.notion.blockconverter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import notion.api.v1.model.common.RichTextColor;
import notion.api.v1.model.pages.PageProperty;

@AllArgsConstructor
@Builder
@Getter
public class CheckAnnotations {
	private Boolean bold; //**
	private Boolean italic;  //*
	private Boolean strikethrough; //~~
	private Boolean underline; // <u> </u>
	private Boolean code; //` `
	private RichTextColor color; //TODO: notYet
	private Boolean equation;

	// PageProperty.RichText.Annotations annotations
	public CheckAnnotations(PageProperty.RichText richText) {
		this.bold = richText.getAnnotations().getBold();
		this.italic = richText.getAnnotations().getItalic();
		this.strikethrough = richText.getAnnotations().getStrikethrough();
		this.underline = richText.getAnnotations().getUnderline();
		this.code = richText.getAnnotations().getCode();
		this.color = richText.getAnnotations().getColor();
		this.equation = richText.getType().toString() == "equation";
	}

	public String applyAnnotations(PageProperty.RichText text) {
		String returnText = text.getPlainText();
		if (returnText.isBlank()) {
			return returnText;
		}
		if (code) {
			returnText = "`" + returnText + "`";
		}
		if (bold) {
			returnText = "**" + returnText + "**";
		}
		if (italic) {
			returnText = "*" + returnText + "*";
		}
		if (strikethrough) {
			returnText = "~~" + returnText + "~~";
		}
		if (underline) {
			returnText = "<u>" + returnText + "</u>";
		}
		if (equation) {
			returnText = "$" + returnText + "$";
		}
		return returnText;
	}
}
