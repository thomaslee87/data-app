package com.intellbit.utils;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

public class SerializationUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(SerializationUtil.class);

	private static final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

	public static final Gson longDateGson = new GsonBuilder().registerTypeAdapter(Date.class,
			new JsonDeserializer<Date>() {
				public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
					return new Date(json.getAsJsonPrimitive().getAsLong());
				}
			}).setDateFormat(DateFormat.LONG).create();

	public static JsonObject string2JsObj(String jsonStr) {
		return gson.fromJson(jsonStr, JsonElement.class).getAsJsonObject();
	}

	public static String gson2String(Object object) {
		return gson.toJson(object);
	}

	/**
	 * 获取 new TypeToken<Collection<Integer>>(){}.getType();
	 **/
	public static <T> T gson2Object(String jsonString, Type type) {
		try {
			return gson.fromJson(jsonString, type);
		} catch (JsonSyntaxException e) {
			LOGGER.warn("Gson string to object error, string : {} to object type {}", jsonString, type);
			throw  e;
		}
	}
	
	/**
	 * 获取 new TypeToken<Collection<Integer>>(){}.getType();
	 **/
	public static <T> T dateLong2Object(String jsonString, Type type) {
		try {
			return longDateGson.fromJson(jsonString, type);
		} catch (JsonSyntaxException e) {
			LOGGER.warn("Gson string to object error, string : {} to object type {}", jsonString, type);
			throw  e;
		}
	}

    public static String formateDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");// "yyyy-MM-dd"
        String date_ = sdf.format(date);
        return date_;
    }
}
