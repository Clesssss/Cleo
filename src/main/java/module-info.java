module com.example.prototype {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;

    opens com.example.prototype to javafx.fxml;
    exports com.example.prototype;
    exports com.example.prototype.entity;
    opens com.example.prototype.entity to javafx.fxml;
    exports com.example.prototype.dao;
    opens com.example.prototype.dao to javafx.fxml;
}