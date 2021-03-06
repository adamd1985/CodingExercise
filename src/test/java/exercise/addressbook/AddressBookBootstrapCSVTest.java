package exercise.addressbook;

import java.io.File;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import exercise.addressbook.datarepositories.ContactRepository;
import exercise.addressbook.model.Contact;
import exercise.addressbook.services.AddressBookBootstrapCSVImpl;
import exercise.addressbook.services.AddressBookBootstrapStrategy;

/**
 * Tests that the bootstrapping correctly happens and all correct data is
 * available on the DB.
 * 
 * @author adam
 * @version 1
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:beans.test.addressbook.xml" })
@TransactionConfiguration
public class AddressBookBootstrapCSVTest {

	private AddressBookBootstrapStrategy testClass = null;
	private File csvFile = null;

	@Autowired
	private ContactRepository repo;

	// ========================================================================

	/**
	 * Given a CSV file with 5 healthy records, then the test should add these
	 * to the database and retrieve all 5 of them.
	 * 
	 * @throws Exception
	 */
	@Transactional
	@Test
	public void testBootingOfValidData() throws Exception {

		this.csvFile = new ClassPathResource("test.addressbook.csv").getFile();
		this.testClass = new AddressBookBootstrapCSVImpl(this.csvFile,
				this.repo);

		this.testClass.boot();
		List<Contact> contacts = this.repo.findAll();

		Assert.assertNotNull(contacts);
		Assert.assertEquals(5, contacts.size());
	}

	/**
	 * Given a CSV file with 1 healthy and 4 malformed (empty commas, missing
	 * info, and malformed date or gender), then the test should add only the
	 * healthy one to the database and retrieve it only.
	 * 
	 * @throws Exception
	 */
	@Transactional
	@Test
	public void testBootingOfInValidData() throws Exception {

		this.csvFile = new ClassPathResource("test.invaliddata.addressbook.csv")
				.getFile();
		this.testClass = new AddressBookBootstrapCSVImpl(this.csvFile,
				this.repo);

		this.testClass.boot();
		List<Contact> contacts = this.repo.findAll();

		Assert.assertNotNull(contacts);
		Assert.assertEquals(1, contacts.size());
	}

	/**
	 * Given a CSV file with 1 healthy and 4 malformed (empty commas, missing
	 * info, and malformed date or gender), then the test should add only the
	 * healthy one to the database and retrieve it only.
	 * 
	 * @throws Exception
	 */
	@Transactional
	@Test
	public void testBootingOfEmptyData() throws Exception {

		this.csvFile = new ClassPathResource("test.empty.addressbook.csv")
				.getFile();
		this.testClass = new AddressBookBootstrapCSVImpl(this.csvFile,
				this.repo);

		this.testClass.boot();
		List<Contact> contacts = this.repo.findAll();

		Assert.assertNotNull(contacts);
		Assert.assertEquals(0, contacts.size());
	}
}
