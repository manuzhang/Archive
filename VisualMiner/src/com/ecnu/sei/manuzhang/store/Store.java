package com.ecnu.sei.manuzhang.store;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

public class Store {	
	private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	private static MemcacheService memcache = MemcacheServiceFactory.getMemcacheService();

	private static final int chunkSize = 1000;
	
	// find entity by its unique ID
	// this may not be useful in real application
	public static Entity findEntity(Key key) throws EntityNotFoundException {
		Entity entity = (Entity) memcache.get(key);
		if (null == entity) {
			entity = datastore.get(key);

			// caching at read time
			memcache.put(key, entity);
		}
		return entity;
	}

	/*
	 *   select * from kind;
	 */
	public static Iterable<Entity> listEntities(String kind) {
		Query query = new Query(kind);
		FetchOptions options = FetchOptions.Builder.withChunkSize(chunkSize);
		PreparedQuery pq = datastore.prepare(query);
		return pq.asIterable(options);
	}

	/*  
	 *    select * from kind
	 *    where searchBy = searchFor;
	 * 
	 */

	public static Iterable<Entity> listEntities(String kind, String searchBy,
			String searchFor) {

		Query query = new Query(kind);
		if (searchFor != null && !searchFor.equals("")) {
			Filter filter = new FilterPredicate(searchBy, Query.FilterOperator.EQUAL, searchFor);
			query.setFilter(filter);
		}
		PreparedQuery pq = datastore.prepare(query);
		return pq.asIterable();
	}

	/*  
	 *  select column from kind;
	 */

	public static String listColumnAsCSV(String kind, String column) {
		Iterable<Entity> entities = listEntities(kind);

		if (null == entities) {
			return "Error: null entities";
		}

		StringBuilder sb = new StringBuilder();

		for (Entity result : entities) {
			Map<String, Object> properties = result.getProperties();
			sb.append(properties.get(column) + ",");
		}
		int index = sb.lastIndexOf(",");
		if (index >= 0) {
			sb.deleteCharAt(index);
		}
		return sb.toString();
	}
	
	public static void truncate(String kind) {
		Iterable<Entity> entities = listEntities(kind);
		List<Key> keys = new ArrayList<Key>();
		for (Entity entity : entities) {
			keys.add(entity.getKey());
		}
		
		datastore.delete(keys);
	}
}
