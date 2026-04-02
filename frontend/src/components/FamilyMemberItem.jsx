import { MdOutlineDeleteForever, MdCardGiftcard } from "react-icons/md";

export default function FamilyMemberItem({ member, onDelete, onOpenWishes }) {
  return (
    <li className="family-member-item">
      <span className="family-member-info">
        {member.firstName} {member.lastName}
      </span>

      <div className="family-member-actions">
        <div className="family-tooltip">
          <button
            className="family-wish-btn"
            onClick={() => onOpenWishes(member)}
          >
            <MdCardGiftcard />
          </button>
          <span className="family-tooltip-text">View wishes</span>
        </div>

        <div className="family-tooltip">
          <button
            className="family-delete-btn"
            onClick={() => onDelete(member)}
          >
            <MdOutlineDeleteForever />
          </button>
          <span className="family-tooltip-text">Delete member</span>
        </div>
      </div>
    </li>
  );
}
