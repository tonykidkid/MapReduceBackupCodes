package countfrommysql;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.lib.db.DBWritable;

// 使用DBWritable完成同mysql的交互
public class MyDBWritable implements DBWritable, Writable{
	private int id;
	private String name;
	private String txt;
	public void readFields(DataInput in) throws IOException {
		id = in.readInt();
		name = in.readUTF();
		txt = in.readUTF();
	}
	public void write(DataOutput out) throws IOException {
		out.writeInt(id);
		out.writeUTF(name);
		out.writeUTF(txt);
	}
	public void readFields(ResultSet rs) throws SQLException {	// 从mysql读取
		id = rs.getInt(1);
		name = rs.getString(2);
		txt = rs.getString(3);
	}
	public void write(PreparedStatement ppst) throws SQLException {
		ppst.setInt(1, id);
		ppst.setString(2, name);
		ppst.setString(3, txt);
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTxt() {
		return txt;
	}
	public void setTxt(String txt) {
		this.txt = txt;
	}
}
