package com.gofirst.utils;



import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Bean2Map {
	
	/***
	 * List 转换
	 * @param listMap
	 * @param T
	 * @return
	 */
	public static <T> List<T> convertListMap2ListBean(List<Object> listMap, Class<?> T) {
		List<T> beanList = new ArrayList<T>();
		for (Object obj : listMap) {
			@SuppressWarnings("unchecked")
			T sb = (T) obj;
			beanList.add(sb);
		}

		return beanList;
	}

	/***
	 * Map和Object转化
	 * @param map
	 * @param beanClass
	 * @return
	 * @throws Exception
	 */
	public static Object mapToObject(Map<String, Object> map, Class<?> beanClass) throws Exception {    
		  if (map == null)  
	            return null;    
	  
	        Object obj = beanClass.newInstance();  
	  
	        Field[] fields = obj.getClass().getDeclaredFields();   
	        for (Field field : fields) {    
	            int mod = field.getModifiers();    
	            if(Modifier.isStatic(mod) || Modifier.isFinal(mod)){    
	                continue;    
	            }    
	  
	            field.setAccessible(true);    
	            Object object = map.get(field.getName());
	            if(object != null  )
	            field.set(obj, object.toString());   
	        }   
	  
	        return obj;    
    }    
      
    

}
