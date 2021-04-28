package com.eliamercatanti.guesthousebooking.app.swing;

import java.awt.EventQueue;
import java.util.concurrent.Callable;

import com.eliamercatanti.guesthousebooking.controller.BookingController;
import com.eliamercatanti.guesthousebooking.controller.GuestController;
import com.eliamercatanti.guesthousebooking.repository.mongo.BookingMongoRepository;
import com.eliamercatanti.guesthousebooking.repository.mongo.GuestMongoRepository;
import com.eliamercatanti.guesthousebooking.validation.controller.ControllerInputValidator;
import com.eliamercatanti.guesthousebooking.view.swing.GuesthouseSwingView;
import com.mongodb.MongoClient;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(mixinStandardHelpOptions = true)
public class GuesthouseSwingApp implements Callable<Void> {

	@Option(names = { "--mongo-host" }, description = "MongoDB Host Address")
	private String mongoHost = "localhost";

	@Option(names = { "--mongo-port" }, description = "MongoDB Host Port")
	private int mongoPort = 27017;

	@Option(names = { "--db-name" }, description = "Database Name")
	private String databaseName = "guesthouse";

	@Option(names = { "--db-guest-collection" }, description = "Guest Collection Name")
	private String guestCollectionName = "guest";

	@Option(names = { "--db-booking-collection" }, description = "Booking Collection Name")
	private String bookingCollectionName = "booking";

	public static void main(String[] args) {
		new CommandLine(new GuesthouseSwingApp()).execute(args);
	}

	@Override
	public Void call() throws Exception {
		EventQueue.invokeLater(() -> {
			try {
				GuestMongoRepository guestRepository = new GuestMongoRepository(new MongoClient(mongoHost, mongoPort),
						databaseName, guestCollectionName);
				BookingMongoRepository bookingRepository = new BookingMongoRepository(
						new MongoClient(mongoHost, mongoPort), databaseName, bookingCollectionName);
				ControllerInputValidator inputValidation = new ControllerInputValidator();
				GuesthouseSwingView guesthouseView = new GuesthouseSwingView();
				GuestController guestController = new GuestController(guestRepository, guesthouseView, inputValidation);
				BookingController bookingController = new BookingController(bookingRepository, guesthouseView,
						inputValidation);
				guesthouseView.setGuestController(guestController);
				guesthouseView.setBookingController(bookingController);
				guesthouseView.setVisible(true);
				guestController.allGuests();
				bookingController.allBookings();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		return null;
	}

}
