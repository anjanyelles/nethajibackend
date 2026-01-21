import React from "react";
import api from "@/services/api";

const ProfileDetails = ({ profile, loading }) => {
  const formatDateTime = (value) => {
    if (!value) return "-";
    const d = new Date(value);
    if (Number.isNaN(d.getTime())) return "-";
    return d.toLocaleString();
  };

  const safe = (value) => {
    if (value === null || value === undefined || value === "") return "-";
    return value;
  };

  const registrationDate = profile?.userCreatedAt || profile?.createdAt;
  const username = profile?.enrollmentNumber || profile?.email;

  const resolveUrl = (u) => {
    if (!u) return u;
    if (/^https?:\/\//i.test(u)) return u;
    try {
      const base = new URL(api.defaults.baseURL);
      return base.origin + u;
    } catch {
      return u;
    }
  };

  const photoUrl = resolveUrl(profile?.profilePictureUrl);

  return (
    <div className="p-10px md:px-10 md:py-50px mb-30px bg-whiteColor dark:bg-whiteColor-dark shadow-accordion dark:shadow-accordion-dark rounded-5">
      <div className="mb-6 pb-5 border-b-2 border-borderColor dark:border-borderColor-dark">
        <h2 className="text-2xl font-bold text-blackColor dark:text-blackColor-dark">
          My Profile
        </h2>
      </div>

      {photoUrl ? (
        <div className="mb-6 flex items-center gap-4">
          <div className="w-20 h-20 rounded-full overflow-hidden border border-borderColor dark:border-borderColor-dark bg-gray-50 flex items-center justify-center">
            {/* eslint-disable-next-line @next/next/no-img-element */}
            <img src={photoUrl} alt="profile" className="w-full h-full object-cover" />
          </div>
        </div>
      ) : null}

      <div>
        <ul>
          <li className="text-lg text-contentColor dark:text-contentColor-dark leading-1.67 grid grid-cols-1 md:grid-cols-12 gap-x-30px">
            <div className="md:col-start-1 md:col-span-4">
              <span className="inline-block">Registration Date</span>
            </div>
            <div className="md:col-start-5 md:col-span-8">
              <span className="inline-block">
                {loading ? "Loading..." : formatDateTime(registrationDate)}
              </span>
            </div>
          </li>

          <li className="text-lg text-contentColor dark:text-contentColor-dark leading-1.67 grid grid-cols-1 md:grid-cols-12 gap-x-30px mt-15px">
            <div className="md:col-start-1 md:col-span-4">
              <span className="inline-block">First Name</span>
            </div>
            <div className="md:col-start-5 md:col-span-8">
              <span className="inline-block">
                {loading ? "Loading..." : safe(profile?.firstName)}
              </span>
            </div>
          </li>
          <li className="text-lg text-contentColor dark:text-contentColor-dark leading-1.67 grid grid-cols-1 md:grid-cols-12 gap-x-30px mt-15px">
            <div className="md:col-start-1 md:col-span-4">
              <span className="inline-block">Last Name</span>
            </div>
            <div className="md:col-start-5 md:col-span-8">
              <span className="inline-block">
                {loading ? "Loading..." : safe(profile?.lastName)}
              </span>
            </div>
          </li>

          <li className="text-lg text-contentColor dark:text-contentColor-dark leading-1.67 grid grid-cols-1 md:grid-cols-12 gap-x-30px mt-15px">
            <div className="md:col-start-1 md:col-span-4">
              <span className="inline-block">Username</span>
            </div>
            <div className="md:col-start-5 md:col-span-8">
              <span className="inline-block">
                {loading ? "Loading..." : safe(username)}
              </span>
            </div>
          </li>

          <li className="text-lg text-contentColor dark:text-contentColor-dark leading-1.67 grid grid-cols-1 md:grid-cols-12 gap-x-30px mt-15px">
            <div className="md:col-start-1 md:col-span-4">
              <span className="inline-block">Email</span>
            </div>
            <div className="md:col-start-5 md:col-span-8">
              <span className="inline-block">
                {loading ? "Loading..." : safe(profile?.email)}
              </span>
            </div>
          </li>

          <li className="text-lg text-contentColor dark:text-contentColor-dark leading-1.67 grid grid-cols-1 md:grid-cols-12 gap-x-30px mt-15px">
            <div className="md:col-start-1 md:col-span-4">
              <span className="inline-block">Phone Number</span>
            </div>
            <div className="md:col-start-5 md:col-span-8">
              <span className="inline-block">
                {loading ? "Loading..." : safe(profile?.mobileNumber)}
              </span>
            </div>
          </li>

          <li className="text-lg text-contentColor dark:text-contentColor-dark leading-1.67 grid grid-cols-1 md:grid-cols-12 gap-x-30px mt-15px">
            <div className="md:col-start-1 md:col-span-4">
              <span className="inline-block">Expert</span>
            </div>
            <div className="md:col-start-5 md:col-span-8">
              <span className="inline-block">
                {loading ? "Loading..." : safe(profile?.state)}
              </span>
            </div>
          </li>

          <li className="text-lg text-contentColor dark:text-contentColor-dark leading-1.67 grid grid-cols-1 md:grid-cols-12 gap-x-30px mt-15px">
            <div className="md:col-start-1 md:col-span-4">
              <span className="inline-block">Biography</span>
            </div>
            <div className="md:col-start-5 md:col-span-8">
              <span className="inline-block">
                {loading ? "Loading..." : safe(profile?.address)}
              </span>
            </div>
          </li>
        </ul>
      </div>
    </div>
  );
};

export default ProfileDetails;
