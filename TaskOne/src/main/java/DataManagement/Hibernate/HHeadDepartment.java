package DataManagement.Hibernate;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="HHeadDepartment")
public class HHeadDepartment extends HUser{

	@OneToOne( cascade = CascadeType.ALL )
	@JoinColumn( name="IDteam" )
	HTeam myTeam;
	
}
