package RankedRetrieval.model;

import javax.persistence.*;
import javax.sql.rowset.serial.SerialBlob;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.SQLException;

@Entity
public class Resources implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String link;
    @Lob
    @Column(columnDefinition = "BLOB")
    private Blob title;
    @Lob
    @Column(columnDefinition = "BLOB")
    private Blob body;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() throws IOException, SQLException {
        return tostring(title);
    }

    public void setTitle(String title) throws SQLException {
        this.title = toBlob(title);
    }

    public String getBody() throws IOException, SQLException {
        return tostring(body);
    }

    public void setBody(String body) throws SQLException {
        this.body = toBlob(body);
    }

    public String tostring(Blob b) throws SQLException, IOException {
        StringBuffer strOut = new StringBuffer();
        String aux;
// We access to stream, as this way we don't have to use the CLOB.length() which is slower...
// assuming blob = resultSet.getBlob("somefield");
        BufferedReader br = new BufferedReader(new InputStreamReader(b.getBinaryStream()));
        while ((aux = br.readLine()) != null) {
            strOut.append(aux);
        }
        return strOut.toString();
    }

    public Blob toBlob(String str) throws SQLException {
        byte byte_string[] = str.getBytes();
        Blob blob = new SerialBlob(byte_string);
        return blob;
    }
}