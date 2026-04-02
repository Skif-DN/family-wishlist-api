import { useState, useEffect, useRef } from "react";
import { useLocation, useNavigate } from "react-router-dom";

import {
  getWishesByOwner,
  createWish,
  updateWish,
  deleteWish,
  fulfillWish,
  getWishStats,
} from "../services/api";

import AddWishModal from "../components/AddWishModal";
import ConfirmModalPin from "../components/ui/ConfirmModalPin";
import InfoModal from "../components/ui/InfoModal";

import logo from "../images/FamilyWishlistLogo.png";
import {
  IoInformationCircleOutline,
  IoArrowBackSharp,
  IoArrowUpSharp,
  IoCheckmarkCircleOutline,
} from "react-icons/io5";
import { MdCardGiftcard, MdOutlineDeleteForever, MdEdit } from "react-icons/md";
import { FcOk } from "react-icons/fc";
import { PiSmileyMeh } from "react-icons/pi";

import Loader from "../components/ui/Loader";
import Button from "../components/ui/Button";
import "./Wishes.css";

export default function Wishes() {
  const location = useLocation();
  const navigate = useNavigate();
  const person = location.state?.person;

  const [wishes, setWishes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [loadingMore, setLoadingMore] = useState(false);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [sortField, setSortField] = useState("title");
  const [sortDir, setSortDir] = useState("asc");

  const [wishStats, setWishStats] = useState({
    totalWishes: 0,
    fulfilledWishes: 0,
  });
  const [loadingStats, setLoadingStats] = useState(true);

  const [showAddModal, setShowAddModal] = useState(false);
  const [editingWish, setEditingWish] = useState(null);
  const [newWish, setNewWish] = useState({
    title: "",
    description: "",
    pin: "",
  });

  const [showConfirmFulfill, setShowConfirmFulfill] = useState(false);
  const [wishToFulfill, setWishToFulfill] = useState(null);

  const [showConfirmDelete, setShowConfirmDelete] = useState(false);
  const [wishToDelete, setWishToDelete] = useState(null);

  const [showInfo, setShowInfo] = useState(false);
  const [infoText, setInfoText] = useState("");
  const [infoContext, setInfoContext] = useState("");
  const [showScrollBtn, setShowScrollBtn] = useState(false);
  const [hideFulfilled, setHideFulfilled] = useState(false);
  const [menuActive, setMenuActive] = useState(false);

  const observerRef = useRef();

  const resetWish = () => {
    setNewWish({
      title: "",
      description: "",
      pin: "",
    });
  };

  const refreshStats = async () => {
    try {
      const data = await getWishStats(person.id);
      setWishStats(data);
    } catch (err) {
      console.error(err);
    }
  };

  const formatDate = (date) =>
    new Date(date).toLocaleString("uk-UA", {
      day: "2-digit",
      month: "2-digit",
      year: "numeric",
      hour: "2-digit",
      minute: "2-digit",
    });

  const handleScroll = () => {
    if (window.scrollY > 200) {
      setShowScrollBtn(true);
    } else {
      setShowScrollBtn(false);
    }
  };

  const handleLogout = () => {
    navigate("/family-members");
  };

  const loadWishes = async (pageToLoad = page) => {
    if (pageToLoad === 0) setLoading(true);
    else setLoadingMore(true);

    const minLoadingTime = new Promise((resolve) => setTimeout(resolve, 700));

    try {
      const data = await getWishesByOwner(
        person.id,
        pageToLoad,
        sortField,
        sortDir,
      );

      await minLoadingTime;

      if (pageToLoad === 0) {
        setWishes(data.content);
      } else {
        setWishes((prev) => [...prev, ...data.content]);
      }

      setTotalPages(data.totalPages);
    } catch (err) {
      console.error(err);
    } finally {
      if (pageToLoad === 0) {
        setLoading(false);
      } else {
        setLoadingMore(false);
      }
    }
  };

  useEffect(() => {
    setPage(0);
    loadWishes(0);
  }, [sortField, sortDir]);

  useEffect(() => {
    if (!observerRef.current) return;

    const observer = new IntersectionObserver(
      (entries) => {
        const first = entries[0];

        if (first.isIntersecting && page + 1 < totalPages && !loadingMore) {
          const nextPage = page + 1;
          setPage(nextPage);
          loadWishes(nextPage);
        }
      },
      { threshold: 0.1 },
    );

    observer.observe(observerRef.current);

    return () => observer.disconnect();
  }, [wishes, page, totalPages, loadingMore]);

  useEffect(() => {
    async function fetchStats() {
      try {
        const data = await getWishStats(person.id);
        setWishStats(data);
      } catch (err) {
        console.error(err);
      } finally {
        setLoadingStats(false);
      }
    }

    fetchStats();
  }, [person.id]);

  useEffect(() => {
    window.addEventListener("scroll", handleScroll);

    return () => {
      window.removeEventListener("scroll", handleScroll);
    };
  }, []);

  const handleAddWish = async () => {
    if (!newWish.title.trim() || !newWish.description.trim()) {
      setShowAddModal(false);
      setInfoText("Please fill the title and description");
      setInfoContext("add");
      setShowInfo(true);
      return;
    }
    try {
      await createWish({
        ownerId: person.id,
        title: newWish.title,
        description: newWish.description,
        pin: newWish.pin,
      });

      setShowAddModal(false);

      resetWish();

      await Promise.all([loadWishes(), refreshStats()]);
    } catch (err) {
      if (err.message.includes("Invalid PIN")) {
        setShowAddModal(false);
        setInfoText("Invalid PIN!");
        setInfoContext("add");
        setShowInfo(true);
      } else {
        alert(err.message);
      }
    }
  };

  const handleEditClick = (wish) => {
    setEditingWish(wish);
    setNewWish({
      title: wish.title,
      description: wish.description,
      pin: "",
    });
    setShowAddModal(true);
  };

  const handleUpdateWish = async () => {
    if (!newWish.title.trim() || !newWish.description.trim()) {
      setShowAddModal(false);
      setInfoText("Please fill the title and description");
      setInfoContext("edit");
      setShowInfo(true);
      return;
    }
    try {
      await updateWish(editingWish.id, {
        ownerId: person.id,
        title: newWish.title,
        description: newWish.description,
        pin: newWish.pin,
      });

      setShowAddModal(false);
      setEditingWish(null);
      resetWish();

      await Promise.all([loadWishes(), refreshStats()]);
    } catch (err) {
      if (err.message.includes("Invalid PIN")) {
        setShowAddModal(false);
        setInfoText("Invalid PIN!");
        setInfoContext("edit");
        setShowInfo(true);
      } else {
        alert(err.message);
      }
    }
  };

  const handleDeleteWish = (wish) => {
    setWishToDelete(wish);
    setShowConfirmDelete(true);
  };

  const handleConfirmDelete = async (pin) => {
    try {
      await deleteWish(wishToDelete.id, pin);
      setShowConfirmDelete(false);
      setWishToDelete(null);

      await Promise.all([loadWishes(), refreshStats()]);
    } catch (err) {
      setShowConfirmDelete(false);

      setInfoText("Invalid PIN!");
      setInfoContext("delete");
      setShowInfo(true);
    }
  };

  const handleFulfillWish = (wish) => {
    setWishToFulfill(wish);
    setShowConfirmFulfill(true);
  };

  const handleConfirmFulfill = async (pin) => {
    try {
      await fulfillWish(wishToFulfill.id, pin);

      setShowConfirmFulfill(false);
      setWishToFulfill(null);

      await Promise.all([loadWishes(), refreshStats()]);
    } catch (err) {
      setShowConfirmFulfill(false);

      setInfoText("Invalid PIN!");
      setInfoContext("fulfill");
      setShowInfo(true);
    }
  };

  const handleScrollToTop = () => {
    window.scrollTo({ top: 0, behavior: "smooth" });
  };

  if (!person) {
    return <p>No person selected</p>;
  }

  if (loading) {
    return <Loader fullScreen text="Loading wishes..." />;
  }

  const percent =
    wishStats.totalWishes === 0
      ? 0
      : Math.round((wishStats.fulfilledWishes / wishStats.totalWishes) * 100);

  const buttons = [
    {
      label: "+ Add Wish",
      variant: "add",
      onClick: () => setShowAddModal(true),
    },
    {
      label: "Sort by title",
      field: "title",
      variant: "secondary",
      onClick: () => {
        setSortField("title");
        setSortDir("asc");
        setPage(0);
      },
    },
    {
      label: "Sort by date",
      variant: "secondary",
      field: "createdAt",
      onClick: () => {
        setSortField("createdAt");
        setSortDir("desc");
        setPage(0);
      },
    },
    {
      label: "Sort by status",
      field: "fulfilledAt",
      variant: "secondary",
      onClick: () => {
        setSortField("fulfilledAt");
        setSortDir("asc");
        setPage(0);
      },
    },
    {
      label: hideFulfilled ? "Show completed" : "Hide completed",
      variant: "secondary",
      onClick: () => {
        setHideFulfilled((prev) => !prev);
        setPage(0);
        loadWishes(0);
      },
    },
  ];

  const renderTextWithLinks = (text) => {
    const urlRegex = /(https?:\/\/[^\s]+)/g;

    return text.split(urlRegex).map((part, index) => {
      if (part.match(urlRegex)) {
        return (
          <a key={index} href={part} target="_blank" rel="noopener noreferrer">
            {part}
          </a>
        );
      }
      return part;
    });
  };

  return (
    <div className="wishes">
      <Button className="wishes-back-btn" variant="back" onClick={handleLogout}>
        <IoArrowBackSharp />
      </Button>
      {showScrollBtn && (
        <Button
          className="wishes-up-btn"
          variant="up"
          onClick={handleScrollToTop}
        >
          <IoArrowUpSharp />
        </Button>
      )}
      <div>
        <img src={logo} className="wishes-logo" alt="logo" />
      </div>

      <div>
        <div className="wishes-info-group">
          <p className="wishes-owner">
            {person.firstName} {person.lastName}
          </p>
          <span className="wishes-info-icon">
            <IoInformationCircleOutline />
            <span className="wishes-info-tooltip">
              Birth date:{" "}
              {new Date(person.birthDate).toLocaleDateString("uk-UA")}
              <br />
              Gender: {person.gender}
            </span>
          </span>

          <span className="wishes-info-icon">
            <MdCardGiftcard />
            <span className="wishes-info-tooltip">
              Total wishes: {wishStats.totalWishes}
              <br />
              Completed wishes: {wishStats.fulfilledWishes}
              <br />
              Progress: {percent}%
            </span>
          </span>
        </div>
      </div>
      <div className="wishes-border-line"></div>
      <div className="wishes-toolbar">
        {buttons.map((btn, i) => (
          <Button
            className={`desktop-buttons ${
              btn.field === sortField ? "active-sort" : ""
            }`}
            key={i}
            variant={btn.variant}
            onClick={btn.onClick}
          >
            {btn.label}
          </Button>
        ))}
      </div>

      <div className="wishes-menu-bar">
        <div
          className={`hamburger ${menuActive ? "change" : ""}`}
          onClick={() => setMenuActive((prev) => !prev)}
        >
          <div className="bar1"></div>
          <div className="bar2"></div>
          <div className="bar3"></div>
        </div>

        <div className={`mobile-toolbar ${menuActive ? "active" : ""}`}>
          {buttons.map((btn, i) => (
            <Button
              className={`mobile-buttons ${
                btn.field === sortField ? "active-sort" : ""
              }`}
              key={i}
              variant={btn.variant}
              onClick={() => {
                btn.onClick();
                setMenuActive(false);
              }}
            >
              {btn.label}
            </Button>
          ))}
        </div>
      </div>
      <div className="wishes-block">
        {loading ? (
          <Loader fullScreen text="Loading wishes..." />
        ) : wishes.length === 0 ? (
          <div className="wishes-members-title-block">
            <p className="wishes-members-title">
              {`${person.firstName} doesn't have any wishes yet`}
            </p>
            <span className="wish-member-title-smile">
              <PiSmileyMeh />
            </span>
          </div>
        ) : (
          <ul className="wishes-list">
            <p className="wishes-title">My wishes</p>
            {wishes
              .filter((wish) => !hideFulfilled || !wish.fulfilled)
              .map((wish) => (
                <li key={wish.id} className="wishes-item">
                  <div className="wishes-text">
                    <h3>{wish.title}</h3>
                    <div className="wish-text-line"></div>
                    <p>{renderTextWithLinks(wish.description)}</p>
                    <div className="wish-text-line"></div>
                    <span className="wishes-date">
                      Created: {formatDate(wish.createdAt)}
                    </span>
                    {wish.fulfilled && (
                      <span className="wishes-fulfilled-date">
                        Fulfilled: {formatDate(wish.fulfilledAt)}
                      </span>
                    )}
                  </div>

                  <div className="wishes-actions">
                    {!wish.fulfilled && (
                      <div className="wishes-tooltip">
                        <button
                          className="wishes-edit-btn"
                          onClick={() => handleEditClick(wish)}
                        >
                          <MdEdit />
                        </button>
                        <span className="wishes-tooltip-text">Edit wish</span>
                      </div>
                    )}
                    <div className="wishes-tooltip">
                      <button
                        className={`wishes-fulfill-btn ${wish.fulfilled ? "done" : ""}`}
                        onClick={() =>
                          !wish.fulfilled && handleFulfillWish(wish)
                        }
                        disabled={wish.fulfilled}
                      >
                        {wish.fulfilled ? (
                          <FcOk />
                        ) : (
                          <IoCheckmarkCircleOutline />
                        )}
                        <span className="wishes-tooltip-text">
                          {wish.fulfilled
                            ? "Wish is completed"
                            : "Mark as completed"}
                        </span>
                      </button>
                    </div>
                    <div className="wishes-tooltip">
                      <button
                        className="wishes-delete-btn"
                        onClick={() => handleDeleteWish(wish)}
                      >
                        <MdOutlineDeleteForever />
                      </button>
                      <span className="wishes-tooltip-text">Delete wish</span>
                    </div>
                  </div>
                </li>
              ))}
            <div ref={observerRef} style={{ height: "1rem" }}></div>
          </ul>
        )}
      </div>
      {showConfirmDelete && wishToDelete && (
        <ConfirmModalPin
          text={`Enter PIN to delete "${wishToDelete.title}"`}
          onConfirm={(pin) => handleConfirmDelete(pin)}
          onCancel={() => {
            setShowConfirmDelete(false);
            setWishToDelete(null);
          }}
        />
      )}

      {showInfo && (
        <InfoModal
          text={infoText}
          onClose={() => {
            setShowInfo(false);
            if (infoContext === "add" || infoContext === "edit") {
              setShowAddModal(true);
            }
            if (infoContext === "delete") {
              setShowConfirmDelete(true);
            }
            if (infoContext === "fulfill") {
              setShowConfirmFulfill(true);
            }

            setInfoContext("");
          }}
        />
      )}

      {showAddModal && (
        <AddWishModal
          newWish={newWish}
          setNewWish={setNewWish}
          editingWish={editingWish}
          onSubmit={editingWish ? handleUpdateWish : handleAddWish}
          onClose={() => {
            setShowAddModal(false);
            setEditingWish(null);
            resetWish();
          }}
        />
      )}

      {showConfirmFulfill && wishToFulfill && (
        <ConfirmModalPin
          text={`Enter PIN to fulfill "${wishToFulfill.title}"`}
          onConfirm={(pin) => handleConfirmFulfill(pin)}
          onCancel={() => {
            setShowConfirmFulfill(false);
            setWishToFulfill(null);
          }}
        />
      )}
    </div>
  );
}
