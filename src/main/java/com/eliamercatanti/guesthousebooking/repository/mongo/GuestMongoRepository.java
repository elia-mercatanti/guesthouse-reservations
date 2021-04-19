package com.eliamercatanti.guesthousebooking.repository.mongo;

import java.util.List;

import com.eliamercatanti.guesthousebooking.model.Guest;
import com.eliamercatanti.guesthousebooking.repository.GuestRepository;
import com.mongodb.MongoClient;

public class GuestMongoRepository implements GuestRepository {

	public GuestMongoRepository(MongoClient client, String databaseName, String collectionName) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Guest> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(Guest guest) {
		// TODO Auto-generated method stub

	}

	@Override
	public Guest findById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(String id) {
		// TODO Auto-generated method stub

	}

}
