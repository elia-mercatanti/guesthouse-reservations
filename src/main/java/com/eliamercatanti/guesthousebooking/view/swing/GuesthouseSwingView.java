package com.eliamercatanti.guesthousebooking.view.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.format.DateTimeFormatter;
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

import com.eliamercatanti.guesthousebooking.controller.BookingController;
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
	private transient GuestController guestController;
	private JList<Guest> listGuest;
	private DefaultComboBoxModel<Guest> comboBoxGuestsModel;
	private JButton btnAddBooking;
	private JComboBox<Integer> comBoxNumberOfGuests;
	private JComboBox<Room> comBoxRoom;
	private JComboBox<Guest> comBoxGuest;
	private DefaultListModel<Booking> listBookingsModel;
	private JButton btnSearchByDates;
	private JButton btnSearchByRoom;
	private JButton btnSerchByGuest;

	transient BookingController bookingController;
	private JList<Booking> listBookings;

	public GuesthouseSwingView() {
		setPreferredSize(new Dimension(900, 600));
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
		btnAddGuest.addActionListener(e -> guestController.newGuest(textFirstName.getText(), textLastName.getText(),
				textEmail.getText(), textTelephoneNumber.getText()));
		btnAddGuest.setEnabled(false);
		btnAddGuest.setName("addGuestButton");

		JScrollPane guestsScrollPane = new JScrollPane();
		guestsScrollPane.setName("guestsScrollPane");

		JButton btnDeleteGuest = new JButton("Delete Guest");
		btnDeleteGuest.addActionListener(e -> guestController.deleteGuest(listGuest.getSelectedValue()));
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
		listGuest = new JList<>(listGuestsModel);
		listGuest.addListSelectionListener(e -> btnDeleteGuest.setEnabled(listGuest.getSelectedIndex() != -1));
		listGuest.setName("guestsList");
		listGuest.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listGuest.setCellRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				Guest guest = (Guest) value;
				return super.getListCellRendererComponent(list, getGuestListDisplayString(guest), index, isSelected,
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
		ActionListener btnAddBookingEnabler = e -> btnAddBooking.setEnabled(!textCheckInDate.getText().trim().isEmpty()
				&& !textCheckOutDate.getText().trim().isEmpty() && (comBoxNumberOfGuests.getSelectedIndex() != -1)
				&& (comBoxRoom.getSelectedIndex() != -1) && (comBoxGuest.getSelectedIndex() != -1));
		KeyAdapter btnSearchByDatesEnabler = new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				btnSearchByDates.setEnabled(
						!textCheckInDate.getText().trim().isEmpty() && !textCheckOutDate.getText().trim().isEmpty());
			}
		};
		textCheckInDate.addActionListener(btnAddBookingEnabler);
		textCheckInDate.addKeyListener(btnSearchByDatesEnabler);
		textCheckInDate.setName("checkInDateTextBox");
		textCheckInDate.setColumns(10);

		textCheckOutDate = new JTextField();
		textCheckOutDate.setName("checkOutDateTextBox");
		textCheckOutDate.setColumns(10);
		textCheckOutDate.addActionListener(btnAddBookingEnabler);
		textCheckOutDate.addKeyListener(btnSearchByDatesEnabler);

		comBoxNumberOfGuests = new JComboBox<>();
		comBoxNumberOfGuests.setModel(new DefaultComboBoxModel<>(new Integer[] { 1, 2, 3, 4 }));
		comBoxNumberOfGuests.setName("numberOfGuestsComBox");
		comBoxNumberOfGuests.setSelectedIndex(-1);
		comBoxNumberOfGuests.addActionListener(btnAddBookingEnabler);

		comBoxRoom = new JComboBox<>();
		comBoxRoom.setModel(new DefaultComboBoxModel<>(Room.values()));
		comBoxRoom.setSelectedIndex(-1);
		comBoxRoom.addActionListener(btnAddBookingEnabler);
		comBoxRoom.addActionListener(e -> btnSearchByRoom.setEnabled(comBoxRoom.getSelectedIndex() != -1));
		comBoxRoom.setName("roomComBox");

		JLabel lblNumberOfGuests = new JLabel("N. of Guests");
		lblNumberOfGuests.setName("numberOfGuestsLabel");

		JLabel lblRoom = new JLabel("Room");
		lblRoom.setName("roomLabel");

		comboBoxGuestsModel = new DefaultComboBoxModel<>();
		comBoxGuest = new JComboBox<>(comboBoxGuestsModel);
		comBoxGuest.setSelectedIndex(-1);
		comBoxGuest.addActionListener(e -> btnSerchByGuest.setEnabled(comBoxGuest.getSelectedIndex() != -1));
		comBoxGuest.setName("guestComBox");
		comBoxGuest.addActionListener(btnAddBookingEnabler);
		comBoxGuest.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(final JList<?> list, Object value, final int index,
					final boolean isSelected, final boolean cellHasFocus) {
				if (value != null) {
					Guest guest = (Guest) (value);
					return super.getListCellRendererComponent(list, getGuestComboBoxDisplayString(guest), index,
							isSelected, cellHasFocus);
				}
				return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

			}
		});

		JLabel lblGuest = new JLabel("Guest");
		lblGuest.setName("guestLabel");

		btnAddBooking = new JButton("Add Booking");
		btnAddBooking.setEnabled(false);
		btnAddBooking.setName("addBookingButton");
		btnAddBooking.addActionListener(e -> bookingController.newBooking(((Guest) comBoxGuest.getSelectedItem()),
				textCheckInDate.getText(), textCheckOutDate.getText(), (int) comBoxNumberOfGuests.getSelectedItem(),
				(Room) comBoxRoom.getSelectedItem()));

		JButton btnDeleteBooking = new JButton("Delete Booking");
		btnDeleteBooking.addActionListener(e -> bookingController.deleteBooking(listBookings.getSelectedValue()));
		btnDeleteBooking.setName("deleteBookingButton");
		btnDeleteBooking.setEnabled(false);

		btnSearchByDates = new JButton("Search by Dates");
		btnSearchByDates.setName("searchByDatesButton");
		btnSearchByDates.setEnabled(false);
		btnSearchByDates.addActionListener(
				e -> bookingController.searchBookingsByDates(textCheckInDate.getText(), textCheckOutDate.getText()));

		JButton btnAllBookings = new JButton("All Bookings");
		btnAllBookings.addActionListener(e -> bookingController.allBookings());
		btnAllBookings.setName("allBookingsButton");

		JScrollPane bookingsScrollPane = new JScrollPane();
		bookingsScrollPane.setName("bookingsScrollPane");

		btnSearchByRoom = new JButton("Search by Room");
		btnSearchByRoom
				.addActionListener(e -> bookingController.searchBookingsByRoom((Room) comBoxRoom.getSelectedItem()));
		btnSearchByRoom.setEnabled(false);
		btnSearchByRoom.setName("searchByRoomButton");

		btnSerchByGuest = new JButton("Search by Guest");
		btnSerchByGuest.addActionListener(
				e -> bookingController.searchBookingsByGuest(((Guest) comBoxGuest.getSelectedItem())));
		btnSerchByGuest.setEnabled(false);
		btnSerchByGuest.setName("searchByGuestButton");
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
												.addComponent(lblGuest).addComponent(comBoxGuest,
														GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
														GroupLayout.PREFERRED_SIZE))
										.addGap(69))
								.addGroup(layoutBookingsPanel.createSequentialGroup().addComponent(btnAddBooking)
										.addPreferredGap(ComponentPlacement.RELATED, 73, Short.MAX_VALUE)
										.addComponent(btnSearchByDates).addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(btnSearchByRoom).addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(btnSerchByGuest)))
						.addContainerGap()));
		layoutBookingsPanel.setVerticalGroup(layoutBookingsPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(layoutBookingsPanel.createSequentialGroup().addContainerGap()
						.addGroup(layoutBookingsPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblCheckInDate).addComponent(lblCheckOutDate)
								.addComponent(lblNumberOfGuests, GroupLayout.PREFERRED_SIZE, 14,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(lblGuest).addComponent(lblRoom))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(layoutBookingsPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(textCheckInDate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(textCheckOutDate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(comBoxNumberOfGuests, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(comBoxGuest, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(comBoxRoom, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(layoutBookingsPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnAddBooking).addComponent(btnSerchByGuest).addComponent(btnSearchByRoom)
								.addComponent(btnSearchByDates))
						.addGap(8).addComponent(bookingsScrollPane, GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(layoutBookingsPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnDeleteBooking).addComponent(btnAllBookings))
						.addContainerGap()));
		listBookingsModel = new DefaultListModel<>();
		listBookings = new JList<>(listBookingsModel);
		listBookings.addListSelectionListener(e -> btnDeleteBooking.setEnabled(listBookings.getSelectedIndex() != -1));
		listBookings.setName("bookingsList");
		listBookings.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listBookings.setCellRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				Booking booking = (Booking) value;
				return super.getListCellRendererComponent(list, getBookingListDisplayString(booking), index, isSelected,
						cellHasFocus);
			}

		});
		bookingsScrollPane.setViewportView(listBookings);
		bookingsPanel.setLayout(layoutBookingsPanel);
		getContentPane().setLayout(groupLayout);
		pack();
	}

	private String getGuestComboBoxDisplayString(Guest guest) {
		return getIdSubstring(guest.getId()) + ", " + guest.getFirstName() + ", " + guest.getLastName();
	}

	private String getIdSubstring(String id) {
		return id.substring(id.length() / 2);
	}

	private String getBookingListDisplayString(Booking booking) {
		String pattern = "dd/MM/yyyy";
		return "id=" + getIdSubstring(booking.getId()) + ", guestId=" + getIdSubstring(booking.getGuestId())
				+ ", checkIn=" + booking.getCheckInDate().format(DateTimeFormatter.ofPattern(pattern)) + ", checkOut="
				+ booking.getCheckOutDate().format(DateTimeFormatter.ofPattern(pattern)) + ", numGuests="
				+ booking.getNumberOfGuests() + ", room=" + booking.getRoom();
	}

	private String getGuestListDisplayString(Guest guest) {
		return "id=" + getIdSubstring(guest.getId()) + ", firstName=" + guest.getFirstName() + ", lastName="
				+ guest.getLastName() + ", email=" + guest.getEmail() + ", telNum=" + guest.getTelephoneNumber();
	}

	@Override
	public void showGuests(List<Guest> guests) {
		listGuestsModel.removeAllElements();
		guests.stream().forEach(listGuestsModel::addElement);
	}

	@Override
	public void guestAdded(Guest guest) {
		listGuestsModel.addElement(guest);
		comboBoxGuestsModel.addElement(guest);
		textFirstName.setText("");
		textLastName.setText("");
		textEmail.setText("");
		textTelephoneNumber.setText("");
		clearErrorLog();
	}

	@Override
	public void guestRemoved(Guest guest) {
		listGuestsModel.removeElement(guest);
		comboBoxGuestsModel.removeElement(guest);
		clearErrorLog();
	}

	private void clearErrorLog() {
		lblErrorLogMessage.setText(" ");
	}

	@Override
	public void showErrorGuestNotFound(String message, Guest guest) {
		lblErrorLogMessage
				.setText(message + ": " + guest.getId() + ", " + guest.getFirstName() + ", " + guest.getLastName());
		listGuestsModel.removeElement(guest);
	}

	public void setGuestController(GuestController guestController) {
		this.guestController = guestController;
	}

	public DefaultListModel<Guest> getListGuestsModel() {
		return listGuestsModel;
	}

	@Override
	public void showError(String message) {
		lblErrorLogMessage.setText(message);
	}

	public DefaultComboBoxModel<Guest> getComboBoxGuestsModel() {
		return comboBoxGuestsModel;
	}

	public DefaultListModel<Booking> getListBookingsModel() {
		return listBookingsModel;
	}

	public void showBookings(List<Booking> bookings) {
		listBookingsModel.removeAllElements();
		clearBookingForm();
		clearErrorLog();
		bookings.stream().forEach(listBookingsModel::addElement);
	}

	private void clearBookingForm() {
		textCheckInDate.setText("");
		textCheckOutDate.setText("");
		comBoxNumberOfGuests.setSelectedIndex(-1);
		comBoxRoom.setSelectedIndex(-1);
		comBoxGuest.setSelectedIndex(-1);
	}

	@Override
	public void showErrorBookingNotFound(String message, Booking booking) {
		String pattern = "dd/MM/yyyy";
		lblErrorLogMessage.setText(message + ": id=" + booking.getId() + ", guestId=" + booking.getGuestId()
				+ ", checkIn=" + booking.getCheckInDate().format(DateTimeFormatter.ofPattern(pattern)) + ", checkOut="
				+ booking.getCheckOutDate().format(DateTimeFormatter.ofPattern(pattern)));
		listBookingsModel.removeElement(booking);
	}

	@Override
	public void bookingAdded(Booking booking) {
		listBookingsModel.addElement(booking);
		clearBookingForm();
		clearErrorLog();
	}

	@Override
	public void bookingRemoved(Booking booking) {
		listBookingsModel.removeElement(booking);
		clearErrorLog();
	}

	public void setBookingController(BookingController bookingController) {
		this.bookingController = bookingController;
	}

}
