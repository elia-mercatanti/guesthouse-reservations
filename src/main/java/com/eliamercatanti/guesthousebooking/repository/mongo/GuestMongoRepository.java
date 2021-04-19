package com.eliamercatanti.guesthousebooking.repository.mongo;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.eliamercatanti.guesthousebooking.model.Guest;
import com.eliamercatanti.guesthousebooking.repository.GuestRepository;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class GuestMongoRepository implements GuestRepository {

	private MongoCollection<Guest> guestCollection;

	public GuestMongoRepository(MongoClient mongoClient, String databaseName, String collectionName) {
		CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
				fromProviders(PojoCodecProvider.builder().automatic(true).build()));
		MongoDatabase database = mongoClient.getDatabase(databaseName).withCodecRegistry(pojoCodecRegistry);
		guestCollection = database.getCollection(collectionName, Guest.class);
	}

	@Override
	public List<Guest> findAll() {
		return StreamSupport.stream(guestCollection.find().spliterator(), false).collect(Collectors.toList());
	}

	@Override
	public void save(Guest guest) {
		guestCollection.insertOne(guest);
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
