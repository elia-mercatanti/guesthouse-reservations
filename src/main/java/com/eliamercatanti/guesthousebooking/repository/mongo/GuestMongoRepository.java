package com.eliamercatanti.guesthousebooking.repository.mongo;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.eliamercatanti.guesthousebooking.model.Guest;
import com.eliamercatanti.guesthousebooking.repository.GuestRepository;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoCollection;

public class GuestMongoRepository implements GuestRepository {

	private MongoCollection<Guest> guestCollection;

	public GuestMongoRepository(MongoClient mongoClient, String databaseName, String collectionName) {
		CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
				fromProviders(PojoCodecProvider.builder().automatic(true).build()));
		guestCollection = mongoClient.getDatabase(databaseName).withCodecRegistry(pojoCodecRegistry)
				.getCollection(collectionName, Guest.class);
	}

	@Override
	public List<Guest> findAll() {
		if (guestCollection.countDocuments() == 0) {
			return Collections.emptyList();
		}
		else {
			return new ArrayList<>();
		}
		
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
