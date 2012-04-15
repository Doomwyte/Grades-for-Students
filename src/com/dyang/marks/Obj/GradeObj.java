package com.dyang.marks.Obj;

public class GradeObj {
	private int id;
	private String grade_name;
	private double grade;
	private int course_id;
	private int category_id;

	public GradeObj(int id, String grade_name, double grade, int course_id, int category_id) {
		setId(id);
		setGrade_name(grade_name);
		setGrade(grade);
		setCourse_id(course_id);
		setCategory_id(category_id);
	}

	public GradeObj() {
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setGrade_name(String grade_name) {
		this.grade_name = grade_name;
	}

	public String getGrade_name() {
		return grade_name;
	}

	public void setGrade(double grade) {
		this.grade = grade;
	}

	public double getGrade() {
		return grade;
	}

	public void setCourse_id(int course_id) {
		this.course_id = course_id;
	}

	public int getCourse_id() {
		return course_id;
	}

	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}

	public int getCategory_id() {
		return category_id;
	}
}
