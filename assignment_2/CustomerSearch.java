import java.util.List;
import java.util.ArrayList;
import java.util.StringJoiner;

public class CustomerSearch {

    // Assuming this list is coming from db
    private List<Customer> customers;

    public List<Customer> searchByCountry(String country) {
        List<Customer> customerList = new ArrayList<>();

        for (Customer customer : customers) {
            if (customer.getCountry().contains(country)) {
                customerList.add(customer);
            }
        }

        return customerList;
    }

    // Search customer by CompanyName
    public List<Customer> searchByCompanyName(String companyName) {
        List<Customer> customerList = new ArrayList<>();

        for (Customer customer : customers) {
            if (customer.getCompanyName().contains(companyName)) {
                customerList.add(customer);
            }
        }

        return result;
    }

    // Search customer by ContactName
    public List<Customer> searchByContact(String contactName) {
        List<Customer> result = new ArrayList<>();

        for (Customer customer : customers) {
            if (customer.getContactName().contains(contactName)) {
                result.add(customer);
            }
        }

        return result;
    }

    // Export customer data to CSV
    public String exportToCSV(List<Customer> data) {
        StringJoiner sj = new StringJoiner("\n");

        for (Customer item : data) {
            sj.add(String.format("%d,%s,%s,%s", item.getCustomerID(), item.getCompanyName(), item.getContactName(), item.getCountry()));
        }

        return sj.toString();
    }
}
