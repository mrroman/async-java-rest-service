import java.io.StringReader;
import java.io.StringWriter;
import java.util.Optional;

import javax.xml.bind.*;

public class XML {

    public static <T> String marshal(Class<T> t, T obj) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(t);
            Marshaller marshaller = jaxbContext.createMarshaller();
            StringWriter writer = new StringWriter();

            marshaller.marshal(obj, writer);
            return writer.toString();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> Optional<T> unmarshal(Class<T> t, String body) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(t);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            return Optional.of(t.cast(unmarshaller.unmarshal(new StringReader(body))));
        } catch (JAXBException e) {
            return Optional.empty();
        }
    }

}
