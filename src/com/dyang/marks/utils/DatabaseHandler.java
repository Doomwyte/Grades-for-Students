package com.dyang.marks.utils;

import java.util.ArrayList;
import java.util.List;

import com.dyang.marks.Obj.CategoryObj;
import com.dyang.marks.Obj.CourseObj;
import com.dyang.marks.Obj.GradeObj;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

	// Database Name
	private static final String DATABASE_NAME = "gradesManager";

	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Table Names
	private static final String courses_table = "courses";
	private static final String categories_table = "categories";
	private static final String grades_table = "grades";

	// Courses table columns names
	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "name";
	private static final String KEY_CODE = "code";
	private static final String KEY_GPA = "gpa";
	private static final String KEY_COURSE_ID = "course_id";
	private static final String KEY_CATEGORY_ID = "category_id";
	private static final String KEY_GRADE_NAME = "grade_name";
	private static final String KEY_GRADE = "grade";
	private static final String KEY_WEIGHT = "weight";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_COURSES_TABLE = "CREATE TABLE " + courses_table + " (" + KEY_ID + " INTEGER PRIMARY KEY,"
				+ KEY_NAME + " TEXT," + KEY_CODE + " TEXT," + KEY_GPA + " TEXT)";
		String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + categories_table + " (" + KEY_ID + " INTEGER PRIMARY KEY, "
				+ KEY_NAME + " TEXT," + KEY_COURSE_ID + " INTEGER," + KEY_WEIGHT + " DOUBLE)";
		String CREATE_GRADES_TABLE = "CREATE TABLE " + grades_table + " (" + KEY_ID + " INTEGER PRIMARY KEY, "
				+ KEY_GRADE_NAME + " TEXT," + KEY_GRADE + " DOUBLE," + KEY_COURSE_ID + " INTEGER," + KEY_CATEGORY_ID
				+ " INTEGER)";
		db.execSQL(CREATE_COURSES_TABLE);
		db.execSQL(CREATE_CATEGORIES_TABLE);
		db.execSQL(CREATE_GRADES_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + courses_table);
		db.execSQL("DROP TABLE IF EXISTS " + categories_table);
		db.execSQL("DROP TABLE IF EXISTS " + grades_table);
		// Create tables again
		onCreate(db);
		db.close();
	}

	// Adding new course
	public void addCourse(CourseObj course) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ID, course.getId()); // Course ID
		values.put(KEY_NAME, course.getName()); // Course Name
		values.put(KEY_CODE, course.getCode()); // Course Code
		values.put(KEY_GPA, course.getCountAsGpa()); // Count in GPA

		// Inserting Row
		db.insert(courses_table, null, values);
		db.close(); // Closing database connection
	}

	// Adding category
	public void addCategory(CategoryObj categoryObj) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ID, categoryObj.getId()); // Category ID
		values.put(KEY_NAME, categoryObj.getCategoryName()); // Category Name
		values.put(KEY_COURSE_ID, categoryObj.getCourse_id()); // Course ID
		values.put(KEY_WEIGHT, categoryObj.getWeight());

		// Inserting Row
		db.insert(categories_table, null, values);
		db.close();
	}

	// Adding grade
	public void addGrade(GradeObj gradeObj) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ID, gradeObj.getId()); // Grade ID
		values.put(KEY_GRADE_NAME, gradeObj.getGrade_name()); // Grade Name
		values.put(KEY_GRADE, gradeObj.getGrade()); // Grade
		values.put(KEY_COURSE_ID, gradeObj.getCourse_id()); // Course ID
		values.put(KEY_CATEGORY_ID, gradeObj.getCategory_id()); // Category ID

		// Inserting Row
		db.insert(grades_table, null, values);
		db.close();
	}

	// Pre-insert course
	public void preAddCourse(int course_id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(courses_table, KEY_ID + "=?", new String[] { Integer.toString(course_id) });
		db.close();
	}

	// Pre-insert category
	public void preAddCategory(int course_id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(categories_table, KEY_COURSE_ID + "=?", new String[] { Integer.toString(course_id) });
		db.close();
	}

	// Pre-insert grade
	public void preAddGrade(int course_id, int category_id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(grades_table, KEY_COURSE_ID + "=? AND " + KEY_CATEGORY_ID + "=?",
				new String[] { Integer.toString(course_id), Integer.toString(category_id) });
		db.close();
	}

	// Find out max ID
	public int getNextGradeId() {
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.query(grades_table, new String[] { "MAX(" + KEY_ID + ")" }, null, null, null, null, null,
				null);
		if (cursor != null)
			cursor.moveToFirst();

		int maxId = cursor.getInt(0);
		return maxId + 1;
	}

	// Getting single course
	public CourseObj getCourse(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(courses_table, new String[] { KEY_ID, KEY_NAME, KEY_CODE, KEY_GPA }, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		CourseObj course = new CourseObj(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
				cursor.getString(3));

		cursor.close();
		db.close();

		// return course
		return course;
	}

	// Getting all courses
	public List<CourseObj> getAllCourses() {
		List<CourseObj> courseList = new ArrayList<CourseObj>();
		// Select All Query
		String selectQuery = "SELECT * FROM " + courses_table;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				CourseObj course = new CourseObj();
				course.setId(cursor.getInt(0));
				course.setName(cursor.getString(1));
				course.setCode(cursor.getString(2));
				course.setCountAsGpa(cursor.getString(3));
				// Adding course to list
				courseList.add(course);
			} while (cursor.moveToNext());
		}

		db.close();
		cursor.close();

		// return course list
		return courseList;
	}

	// Getting all categories
	public List<CategoryObj> getAllCategories(int course_id) {
		List<CategoryObj> categoryList = new ArrayList<CategoryObj>();
		// Select All Query
		String selectQuery = "SELECT * FROM " + categories_table + " WHERE " + KEY_COURSE_ID + "=" + course_id;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				CategoryObj category = new CategoryObj();
				category.setId(cursor.getInt(0));
				category.setCategoryName(cursor.getString(1));
				category.setCourse_id(cursor.getInt(2));
				category.setWeight(cursor.getDouble(3));
				// Adding category to list
				categoryList.add(category);
			} while (cursor.moveToNext());
		}

		db.close();
		cursor.close();

		// return category list
		return categoryList;
	}

	// Getting all grades
	public List<GradeObj> getAllGrades(int course_id, int category_id) {
		List<GradeObj> gradeList = new ArrayList<GradeObj>();
		// Select All Query
		String selectQuery = "SELECT * FROM " + grades_table + " WHERE " + KEY_COURSE_ID + "=" + course_id + " AND "
				+ KEY_CATEGORY_ID + "=" + category_id;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				GradeObj grade = new GradeObj();
				grade.setId(cursor.getInt(0));
				grade.setGrade_name(cursor.getString(1));
				grade.setGrade(cursor.getDouble(2));
				grade.setCourse_id(cursor.getInt(3));
				grade.setCategory_id(cursor.getInt(4));
				// Adding category to list
				gradeList.add(grade);
			} while (cursor.moveToNext());
		}

		db.close();
		cursor.close();

		// return grade list
		return gradeList;
	}

	// Getting courses count
	public int getCoursesCount() {
		String countQuery = "SELECT * FROM courses";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int count = cursor.getCount();
		cursor.close();
		db.close();

		// return count
		return count;
	}

	// Getting categories count by course ID
	public int getCategoriesCountByCourseId(int course_id) {
		String countQuery = "SELECT * FROM " + categories_table + " WHERE " + KEY_COURSE_ID + "=" + course_id;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int count = cursor.getCount();
		cursor.close();
		db.close();

		// return count
		return count;
	}

	// Getting categories count
	public int getCategoriesCount(int course_id) {
		String countQuery = "SELECT * FROM " + categories_table + " WHERE " + KEY_COURSE_ID + "=" + course_id;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int count = cursor.getCount();
		cursor.close();
		db.close();

		// return count
		return count;
	}

	// Getting categories count
	public int getCategoriesCount() {
		String countQuery = "SELECT * FROM " + categories_table;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int count = cursor.getCount();
		cursor.close();
		db.close();

		// return count
		return count;
	}

	// Getting grades count
	public int getGradesCount(int course_id, int category_id) {
		String countQuery = "SELECT * FROM " + grades_table + " WHERE " + KEY_COURSE_ID + "=" + course_id + " AND "
				+ KEY_CATEGORY_ID + "=" + category_id;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int count = cursor.getCount();
		cursor.close();
		db.close();

		// return count
		return count;
	}

	// Getting grades count
	public int getGradesCount() {
		String countQuery = "SELECT * FROM " + grades_table;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int count = cursor.getCount();
		cursor.close();
		db.close();

		// return count
		return count;
	}

	// Updating single course
	public void updateCourse(CourseObj course) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, course.getName());
		values.put(KEY_CODE, course.getName());
		values.put(KEY_GPA, course.getCountAsGpa());

		// updating row
		db.update(courses_table, values, KEY_ID + " = ?", new String[] { String.valueOf(course.getId()) });

		db.close();
	}

	// Deleting single course
	public void deleteCourse(CourseObj course) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(courses_table, KEY_ID + " = ?", new String[] { String.valueOf(course.getId()) });
		db.delete(categories_table, KEY_COURSE_ID + " = ?", new String[] { String.valueOf(course.getId()) });
		db.delete(grades_table, KEY_COURSE_ID + " = ?", new String[] { String.valueOf(course.getId()) });
		db.close();
	}

	// Delete all courses
	public void deleteAllCourses() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(courses_table, "1", new String[] {});
		db.delete(categories_table, "1", new String[] {});
		db.delete(grades_table, "1", new String[] {});
		db.close();
	}

}
