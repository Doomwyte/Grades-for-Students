package com.dyang.marks.courseObj;

public class CourseObj {
	private int id;
	private String name;
	private String code;
	private String countAsGpa;

	public CourseObj(int id, String name, String code, String countAsGpa) {
		setId(id);
		setName(name);
		setCode(code);
		setCountAsGpa(countAsGpa);
	}

	public CourseObj() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setCountAsGpa(String countAsGpa) {
		this.countAsGpa = countAsGpa;
	}

	public String getCountAsGpa() {
		return countAsGpa;
	}
}
