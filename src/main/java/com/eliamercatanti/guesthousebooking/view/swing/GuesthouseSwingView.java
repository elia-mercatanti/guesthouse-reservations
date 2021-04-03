package com.eliamercatanti.guesthousebooking.view.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import com.eliamercatanti.guesthousebooking.controller.GuestController;
import com.eliamercatanti.guesthousebooking.model.Booking;
import com.eliamercatanti.guesthousebooking.model.Guest;
import com.eliamercatanti.guesthousebooking.model.Room;
import com.eliamercatanti.guesthousebooking.view.GuesthouseView;

public class GuesthouseSwingView extends JFrame implements GuesthouseView {

	private static final long serialVersionUID = 1L;
	private JTextField textFirstName;
	private JTextField textLastName;
	private JTextField textEmail;
	private JTextField textTelephoneNumber;
	private JTextField textCheckInDate;
	private JTextField textCheckOutDate;
	private JButton btnAddGuest;
	private DefaultListModel<Guest> listGuestsModel;
	private JLabel lblErrorLogMessage;

	public GuesthouseSwingView() {
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("Guesthouse Reservations");

		JLabel lblErrorLog = new JLabel("Error Log:");
		lblErrorLog.setName("errorLogLabel");
		lblErrorLog.setFont(new Font("Tahoma", Font.BOLD, 11));

		JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.TOP);
		tabbedPane.setName("tabbedPane");

		lblErrorLogMessage = new JLabel(" ");
		lblErrorLogMessage.setForeground(Color.RED);
		lblErrorLogMessage.setName("errorLogMessageLabel");
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 384, Short.MAX_VALUE)
				.addGroup(groupLayout.createSequentialGroup().addContainerGap().addComponent(lblErrorLog)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(lblErrorLogMessage)
						.addContainerGap(268, Short.MAX_VALUE)));
		groupLayout
				.setVerticalGroup(
						groupLayout.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING,
								groupLayout.createSequentialGroup()
										.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 335, Short.MAX_VALUE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
												.addComponent(lblErrorLog).addComponent(lblErrorLogMessage))
										.addGap(6)));

		JPanel guestsPanel = new JPanel();
		guestsPanel.setName("guestsPanel");
		tabbedPane.addTab("Guests", null, guestsPanel, null);

		JLabel lblFirstName = new JLabel("First Name");
		lblFirstName.setName("firstNameLabel");

		JLabel lblLastName = new JLabel("Last Name");
		lblLastName.setName("lastNameLabel");

		JLabel lblEmail = new JLabel("Email");
		lblEmail.setName("emailLabel");

		JLabel lblTelephoneNumber = new JLabel("Telephone N.");
		lblTelephoneNumber.setName("telephoneNumberLabel");

		textFirstName = new JTextField();
		KeyAdapter btnAddGuestEnabler = new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				btnAddGuest.setEnabled(!textFirstName.getText().trim().isEmpty()
						&& !textLastName.getText().trim().isEmpty() && !textEmail.getText().trim().isEmpty()
						&& !textTelephoneNumber.getText().trim().isEmpty());
			}
		};
		textFirstName.setName("firstNameTextBox");
		textFirstName.setColumns(10);
		textFirstName.addKeyListener(btnAddGuestEnabler);

		textLastName = new JTextField();
		textLastName.setName("lastNameTextBox");
		textLastName.setColumns(10);
		textLastName.addKeyListener(btnAddGuestEnabler);

		textEmail = new JTextField();
		textEmail.setName("emailTextBox");
		textEmail.setColumns(10);
		textEmail.addKeyListener(btnAddGuestEnabler);

		textTelephoneNumber = new JTextField();
		textTelephoneNumber.setName("telephoneNumberTextBox");
		textTelephoneNumber.setColumns(10);
		textTelephoneNumber.addKeyListener(btnAddGuestEnabler);

		btnAddGuest = new JButton("Add Guest");
		btnAddGuest.setEnabled(false);
		btnAddGuest.setName("addGuestButton");

		JScrollPane guestsScrollPane = new JScrollPane();
		guestsScrollPane.setName("guestsScrollPane");

		JButton btnDeleteGuest = new JButton("Delete Guest");
		btnDeleteGuest.setEnabled(false);
		btnDeleteGuest.setName("deleteGuestButton");
		GroupLayout layoutGuestsPanel = new GroupLayout(guestsPanel);
		layoutGuestsPanel.setHorizontalGroup(layoutGuestsPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(layoutGuestsPanel.createSequentialGroup().addContainerGap()
						.addGroup(layoutGuestsPanel.createParallelGroup(Alignment.LEADING)
								.addGroup(layoutGuestsPanel.createSequentialGroup()
										.addComponent(guestsScrollPane, GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE)
										.addContainerGap())
								.addGroup(layoutGuestsPanel.createSequentialGroup().addGroup(layoutGuestsPanel
										.createParallelGroup(Alignment.LEADING)
										.addGroup(layoutGuestsPanel.createSequentialGroup()
												.addGroup(layoutGuestsPanel.createParallelGroup(Alignment.LEADING)
														.addComponent(textFirstName, GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
														.addComponent(lblFirstName))
												.addPreferredGap(ComponentPlacement.RELATED)
												.addGroup(layoutGuestsPanel.createParallelGroup(Alignment.LEADING)
														.addComponent(lblLastName).addComponent(textLastName,
																GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE))
												.addPreferredGap(ComponentPlacement.RELATED)
												.addGroup(layoutGuestsPanel.createParallelGroup(Alignment.LEADING)
														.addComponent(lblEmail)
														.addComponent(textEmail, GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
												.addPreferredGap(ComponentPlacement.RELATED)
												.addGroup(layoutGuestsPanel.createParallelGroup(Alignment.LEADING)
														.addComponent(lblTelephoneNumber)
														.addComponent(textTelephoneNumber, GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
										.addComponent(btnAddGuest).addComponent(btnDeleteGuest)).addGap(113)))));
		layoutGuestsPanel.setVerticalGroup(layoutGuestsPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(layoutGuestsPanel.createSequentialGroup().addContainerGap()
						.addGroup(layoutGuestsPanel.createParallelGroup(Alignment.BASELINE).addComponent(lblFirstName)
								.addComponent(lblTelephoneNumber).addComponent(lblEmail).addComponent(lblLastName))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(layoutGuestsPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(textFirstName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(textLastName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(textEmail, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(textTelephoneNumber, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(btnAddGuest)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(guestsScrollPane, GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(btnDeleteGuest).addGap(6)));

		listGuestsModel = new DefaultListModel<>();
		JList<Guest> listGuest = new JList<>(listGuestsModel);
		listGuest.addListSelectionListener(e -> btnDeleteGuest.setEnabled(listGuest.getSelectedIndex() != -1));
		listGuest.setName("guestsList");
		listGuest.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listGuest.setCellRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				Guest guest = (Guest) value;
				return super.getListCellRendererComponent(list, getGuestDisplayString(guest), index, isSelected,
						cellHasFocus);
			}
		});
		guestsScrollPane.setViewportView(listGuest);
		guestsPanel.setLayout(layoutGuestsPanel);

		JPanel bookingsPanel = new JPanel();
		bookingsPanel.setName("bookingPanel");
		tabbedPane.addTab("Bookings", null, bookingsPanel, null);

		JLabel lblCheckInDate = new JLabel("Check In");
		lblCheckInDate.setName("checkInDateLabel");

		JLabel lblCheckOutDate = new JLabel("Check Out");
		lblCheckOutDate.setName("checkOutDateLabel");

		textCheckInDate = new JTextField();
		textCheckInDate.setName("checkInDateTextBox");
		textCheckInDate.setColumns(10);

		textCheckOutDate = new JTextField();
		textCheckOutDate.setName("checkOutDateTextBox");
		textCheckOutDate.setColumns(10);

		JComboBox<Integer> comBoxNumberOfGuests = new JComboBox<>();
		comBoxNumberOfGuests.setModel(new DefaultComboBoxModel<>(new Integer[] { 1, 2, 3, 4 }));
		comBoxNumberOfGuests.setName("numberOfGuestsComBox");

		JComboBox<Room> comBoxRoom = new JComboBox<>();
		comBoxRoom.setModel(new DefaultComboBoxModel<>(Room.values()));
		comBoxRoom.setName("roomComBox");

		JLabel lblNumberOfGuests = new JLabel("N. of Guests");
		lblNumberOfGuests.setName("numberOfGuestsLabel");

		JLabel lblRoom = new JLabel("Room");
		lblRoom.setName("roomLabel");

		JComboBox<Guest> comBoxGuestId = new JComboBox<>();
		comBoxGuestId.setName("guestIdComBox");

		JLabel lblGuestId = new JLabel("Guest ID");
		lblGuestId.setName("guestIdLabel");

		JButton btnAddBooking = new JButton("Add Booking");
		btnAddBooking.setEnabled(false);
		btnAddBooking.setName("addBookingButton");

		JButton btnDeleteBooking = new JButton("Delete Booking");
		btnDeleteBooking.setName("deleteBookingButton");
		btnDeleteBooking.setEnabled(false);

		JButton btnSearchBookings = new JButton("Search Bookings");
		btnSearchBookings.setName("searchBookingsButton");
		btnSearchBookings.setEnabled(false);

		JButton btnAllBookings = new JButton("All Bookings");
		btnAllBookings.setName("allBookingsButton");
		btnAllBookings.setEnabled(false);

		JScrollPane bookingsScrollPane = new JScrollPane();
		bookingsScrollPane.setName("bookingsScrollPane");
		GroupLayout layoutBookingsPanel = new GroupLayout(bookingsPanel);
		layoutBookingsPanel.setHorizontalGroup(layoutBookingsPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(layoutBookingsPanel.createSequentialGroup().addContainerGap()
						.addGroup(layoutBookingsPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(bookingsScrollPane, GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE)
								.addGroup(layoutBookingsPanel.createSequentialGroup().addComponent(btnDeleteBooking)
										.addPreferredGap(ComponentPlacement.RELATED, 273, Short.MAX_VALUE)
										.addComponent(btnAllBookings))
								.addGroup(layoutBookingsPanel.createSequentialGroup()
										.addGroup(layoutBookingsPanel.createParallelGroup(Alignment.LEADING)
												.addComponent(textCheckInDate, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addComponent(lblCheckInDate))
										.addPreferredGap(ComponentPlacement.RELATED)
										.addGroup(layoutBookingsPanel.createParallelGroup(Alignment.LEADING)
												.addComponent(lblCheckOutDate).addComponent(textCheckOutDate,
														GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(ComponentPlacement.RELATED)
										.addGroup(layoutBookingsPanel.createParallelGroup(Alignment.LEADING)
												.addComponent(comBoxNumberOfGuests, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addComponent(lblNumberOfGuests))
										.addPreferredGap(ComponentPlacement.RELATED)
										.addGroup(layoutBookingsPanel.createParallelGroup(Alignment.LEADING)
												.addComponent(comBoxRoom, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addComponent(lblRoom))
										.addGap(18)
										.addGroup(layoutBookingsPanel.createParallelGroup(Alignment.LEADING)
												.addComponent(lblGuestId).addComponent(comBoxGuestId,
														GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
														GroupLayout.PREFERRED_SIZE))
										.addGap(69))
								.addGroup(layoutBookingsPanel.createSequentialGroup().addComponent(btnAddBooking)
										.addPreferredGap(ComponentPlacement.RELATED).addComponent(btnSearchBookings)))
						.addContainerGap()));
		layoutBookingsPanel.setVerticalGroup(layoutBookingsPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(layoutBookingsPanel.createSequentialGroup().addContainerGap()
						.addGroup(layoutBookingsPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblCheckInDate).addComponent(lblCheckOutDate)
								.addComponent(lblNumberOfGuests, GroupLayout.PREFERRED_SIZE, 14,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(lblGuestId).addComponent(lblRoom))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(layoutBookingsPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(textCheckInDate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(textCheckOutDate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(comBoxNumberOfGuests, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(comBoxGuestId, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(comBoxRoom, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(layoutBookingsPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnAddBooking).addComponent(btnSearchBookings))
						.addGap(8).addComponent(bookingsScrollPane, GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(layoutBookingsPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnDeleteBooking).addComponent(btnAllBookings))
						.addContainerGap()));

		JList<Booking> listBookings = new JList<>();
		listBookings.setName("bookingsList");
		bookingsScrollPane.setViewportView(listBookings);
		bookingsPanel.setLayout(layoutBookingsPanel);
		getContentPane().setLayout(groupLayout);
	}

	private String getGuestDisplayString(Guest guest) {
		return guest.getId() + " - " + guest.getFirstName() + " - " + guest.getLastName() + " - " + guest.getEmail()
				+ " - " + guest.getTelephoneNumber();
	}

	@Override
	public void showAllGuests(List<Guest> guests) {
		guests.stream().forEach(listGuestsModel::addElement);
	}

	@Override
	public void guestAdded(Guest guest) {
		listGuestsModel.addElement(guest);
		clearErrorLog();
	}

	@Override
	public void guestRemoved(Guest guest) {
		listGuestsModel.removeElement(guest);
		clearErrorLog();
	}

	private void clearErrorLog() {
		lblErrorLogMessage.setText(" ");
	}

	@Override
	public void showErrorGuestNotFound(String message, Guest guest) {
		lblErrorLogMessage
				.setText(message + ": " + guest.getId() + " - " + guest.getFirstName() + " - " + guest.getLastName());
		listGuestsModel.removeElement(guest);
	}

	@Override
	public void setGuestController(GuestController guestController) {
		// TODO Auto-generated method stub

	}

	public DefaultListModel<Guest> getListGuestsModel() {
		return listGuestsModel;
	}

	@Override
	public void showError(String message) {
		lblErrorLogMessage.setText(message);
	}

}
