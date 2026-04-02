const API_URL = import.meta.env.VITE_API_URL || "";

async function fetchWithAuth(url, options = {}) {
  const token = localStorage.getItem("token");

  const response = await fetch(`${API_URL}${url}`, {
    ...options,
    headers: {
      "Content-Type": "application/json",
      ...(options.headers || {}),
      Authorization: token ? `Bearer ${token}` : undefined,
    },
  });

  if (response.status === 204) return null;

  let data = null;
  try {
    data = await response.json();
  } catch (err) {
    data = null;
  }

  if (!response.ok) {
    const errorMessage = data?.message || response.statusText || "API Error";
    const error = new Error(errorMessage);
    error.status = response.status;
    throw error;
  }

  return data;
}

export async function register(username, password) {
  return fetchWithAuth("/auth/register", {
    method: "POST",
    body: JSON.stringify({ username, password }),
  });
}

export async function login(username, password) {
  const response = await fetch(`${API_URL}/auth/login`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ username, password }),
  });

  const data = await response.json().catch(() => ({}));

  if (!response.ok) {
    throw new Error(data.message || "Login failed");
  }

  return data.token;
}

// Families
export async function getMyFamily() {
  try {
    return await fetchWithAuth("/families/me");
  } catch (err) {
    if (err.status === 404 || err.status === 409) {
      return null;
    }
    throw err;
  }
}

export async function createFamily(name) {
  return fetchWithAuth("/families", {
    method: "POST",
    body: JSON.stringify({ name }),
  });
}

export async function getFamilyMembers(
  familyId,
  sortBy = "firstName",
  direction = "asc",
) {
  return fetchWithAuth(
    `/persons/family/${familyId}?sortBy=${sortBy}&direction=${direction}`,
  );
}

export async function addPerson(dto) {
  return fetchWithAuth("/persons", {
    method: "POST",
    body: JSON.stringify(dto),
  });
}

export async function deletePerson(personId) {
  return fetchWithAuth(`/persons/${personId}`, {
    method: "DELETE",
  });
}

// Wishes
export async function createWish(dto) {
  return fetchWithAuth("/wishes", {
    method: "POST",
    body: JSON.stringify(dto),
  });
}

export const getWishesByOwner = async (
  personId,
  page,
  sortField,
  sortDir,
  size = 5,
) => {
  return await fetchWithAuth(
    `/wishes/byOwner/${personId}?page=${page}&size=${size}&sort=${sortField},${sortDir}`,
  );
};

export async function updateWish(wishId, dto) {
  return fetchWithAuth(`/wishes/${wishId}`, {
    method: "PUT",
    body: JSON.stringify(dto),
  });
}

export async function deleteWish(wishId, pin) {
  return fetchWithAuth(`/wishes/${wishId}`, {
    method: "DELETE",
    body: JSON.stringify({ pin }),
  });
}

export async function fulfillWish(wishId, pin) {
  return fetchWithAuth(`/wishes/${wishId}/fulfill`, {
    method: "PATCH",
    body: JSON.stringify({ pin }),
  });
}

export async function getWishStats(personId) {
  return fetchWithAuth(`/wishes/stats/${personId}`);
}
