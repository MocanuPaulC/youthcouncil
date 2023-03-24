package be.kdg.youthcouncil.domain.moduleItems;

import be.kdg.youthcouncil.domain.interactions.Reaction;
import be.kdg.youthcouncil.domain.interactions.Share;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

import static javax.persistence.DiscriminatorType.STRING;


@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table (name = "module_items")
@Inheritance (strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn (name = "module_item_type", discriminatorType = STRING, length = 20)
@DiscriminatorValue ("module_item")
public abstract class ModuleItem {
	@Transient
	@Getter (AccessLevel.NONE)
	@Setter (AccessLevel.NONE)
	@ToString.Exclude
	private final Logger logger = LoggerFactory.getLogger(this.getClass());


	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	@Column (name = "module_item_id")
	private long id;
	private String title;
	private String description;
	@Column (name = "is_enabled")
	@ColumnDefault ("false")
	private boolean isEnabled;

	@Column (name = "is_default")
	@ColumnDefault ("false")
	private boolean isDefault;

	//	@JoinColumn (name = "default_module_item")
	//	@OneToOne
	//	private ModuleItem defaultModuleItem;

	@OneToMany
	@ToString.Exclude
	private List<Reaction> reactions;

	@OneToMany
	@ToString.Exclude
	private List<Share> shares;


	public void addReaction(Reaction reaction) {
		logger.debug("Adding reaction " + reaction.getEmoji() + " to moduleItem " + title);
		reactions.add(reaction);
	}

	public void removeReaction(Reaction reaction) {
		logger.debug("Removing reaction " + reaction.getEmoji() + " from moduleItem " + title);
		reactions.remove(reaction);
	}

	public void addShare(Share share) {
		logger.debug("Adding share to moduleItem " + title);
		shares.add(share);
	}

	public void removeShare(Share share) {
		logger.debug("Removing share from moduleItem " + title);
		shares.remove(share);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ModuleItem that = (ModuleItem) o;

		if (!Objects.equals(title, that.title)) return false;
		return Objects.equals(description, that.description);
	}

	@Override
	public int hashCode() {
		int result = title != null ? title.hashCode() : 0;
		result = 31 * result + (description != null ? description.hashCode() : 0);
		return result;
	}
}
