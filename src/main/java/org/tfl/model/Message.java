package org.tfl.model;

import java.time.LocalDateTime;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private String userId;
    private String content;
    private LocalDateTime date;
    private int id;
    private int label;
    private String labelStr;
    
	public Message() {
		super();
	}
	public Message(String userId, String content, LocalDateTime date, int label) {
		super();
		this.userId = userId;
		this.content = content;
		this.date = date;
		this.label = label;
	}
	public Message(int id, String userId, String content, LocalDateTime date, String labelStr) {
		super();
		this.id = id;
		this.userId = userId;
		this.content = content;
		this.date = date;
		this.labelStr = labelStr;
	}
	public Message(String userId, String content, LocalDateTime date, String labelStr) {
		super();
		this.userId = userId;
		this.content = content;
		this.date = date;
		this.labelStr = labelStr;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public LocalDateTime getDate() {
		return date;
	}
	public void setDate(LocalDateTime date) {
		this.date = date;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getLabel() {
		return label;
	}
	public void setLabel(int label) {
		this.label = label;
	}
	public String getLabelStr() {
		return labelStr;
	}
	public void setLabelStr(String labelStr) {
		this.labelStr = labelStr;
	}
    
}
