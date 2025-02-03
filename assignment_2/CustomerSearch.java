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

}

public interface Exporter{
    Report export(List<Customer> list);
}

public class CSVExporter implements Exporter{
    Report export(List<Customer> list){

    }
}