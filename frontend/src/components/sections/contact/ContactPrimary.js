import React from "react";

const ContactPrimary = () => {
  return (
    <section>
      <div className="container">
        <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-[30px] pt-[100px] pb-[20px]">
          {/* Mail Address */}
          <div className="pt-[15px] pr-[35px] pb-[25px] pl-5 lg:pt-10 lg:pb-10 lg:pl-[35px] transition-all duration-300 border border-borderColor dark:border-borderColor-dark shadow-address hover:shadow-address-hover hover:-translate-y-[5px] flex items-center gap-5 lg:gap-[30px]">
            <div>
              {/* Mail Icon */}
              <svg width="64" height="64" viewBox="0 0 64 64" fill="none">
                <g clipPath="url(#clip0)">
                  <rect width="64" height="64" fill="white"></rect>
                  <path d="M63.7 20.34L54.43 13.79V4.23C54.34 3.29 53.61 2.52 52.67 2.39H12.06C11.12 2.52 10.39 3.29 10.3 4.23V13.72L0.662 20.34C0.28 20.62 0.0386 21.05 0 21.52V56.46C0.125 57.42 0.88 58.17 1.84 58.3H62.89C63.7 58.3 63.99 57.34 63.99 56.46V21.52C63.99 21.08 64.07 20.64 63.7 20.34ZM54.43 17.33L60.76 21.67L54.43 26.45V17.33ZM13.24 5.34H51.49V28.73L32.36 43.14L13.24 28.73V5.34ZM10.3 17.25V26.52L3.972 21.67L10.3 17.25ZM2.94 24.68L23.54 40.27L2.94 54.18V24.68ZM6.47 55.35L26.04 42.19L31.26 46.16C31.56 46.39 31.92 46.52 32.29 46.53C32.59 46.53 32.73 46.38 33.03 46.16L38.47 41.97L58.26 55.35H6.47ZM61.05 53.66L40.9 40.13L61.05 24.68V53.66Z" fill="#5F2DED" />
                  <path d="M20.6 14.9H27.22C28.03 14.9 28.69 14.24 28.69 13.43C28.69 12.61 28.03 11.96 27.22 11.96H20.6C19.78 11.96 19.13 12.61 19.13 13.43C19.13 14.24 19.78 14.9 20.6 14.9Z" fill="#5F2DED" />
                  <path d="M20.6 21.52H44.13C44.95 21.52 45.61 20.86 45.61 20.05C45.61 19.23 44.95 18.58 44.13 18.58H20.6C19.78 18.58 19.13 19.23 19.13 20.05C19.13 20.86 19.78 21.52 20.6 21.52Z" fill="#5F2DED" />
                  <path d="M45.61 26.67C45.61 25.85 44.95 25.2 44.13 25.2H20.6C19.78 25.2 19.13 25.85 19.13 26.67C19.13 27.48 19.78 28.14 20.6 28.14H44.13C44.95 28.14 45.61 27.48 45.61 26.67Z" fill="#5F2DED" />
                </g>
                <defs>
                  <clipPath id="clip0"><rect width="64" height="64" fill="white" /></clipPath>
                </defs>
              </svg>
            </div>
            <div>
              <h3 className="text-2xl font-semibold text-blackColor dark:text-blackColor-dark leading-[38px]">Mail address</h3>
              <p className="text-[13px] lg:text-[15px] text-contentColor dark:text-contentColor-dark leading-5">nethajidc@gmail.com</p>
              <p className="text-[15px] text-contentColor dark:text-contentColor-dark leading-[26px]">+91 834 000 7081 </p>
            </div>
          </div>

          {/* Office Address */}
          <div className="pt-[15px] pr-[35px] pb-[25px] pl-5 lg:pt-10 lg:pb-10 lg:pl-[35px] transition-all duration-300 border border-borderColor dark:border-borderColor-dark shadow-address hover:shadow-address-hover hover:-translate-y-[5px] flex items-center gap-5 lg:gap-[30px]">
            <div>
              {/* Office Icon */}
              <svg width="46" height="60" viewBox="0 0 46 60" fill="none">
                <path d="M38.89 48.52C37.09 47.73 34.78 47.09 32.1 46.64C32.88 45.89 33.68 45.09 34.48 44.24C37.67 40.85 40.22 37.39 42.05 33.95C44.37 29.6 45.55 25.27 45.55 21.08C45.55 9.46 35.33 0 22.77 0C10.22 0 0 9.46 0 21.08C0 25.27 1.17 29.6 3.49 33.95C5.32 37.39 7.87 40.85 11.07 44.24C11.86 45.09 12.66 45.89 13.44 46.64C10.77 47.09 8.45 47.73 6.66 48.52C3.25 50.03 2.53 51.75 2.53 52.93C2.53 54.42 3.64 56.54 8.95 58.18C12.66 59.33 17.57 59.96 22.77 59.96C27.97 59.96 32.88 59.33 36.59 58.18C41.9 56.54 43.01 54.42 43.01 52.93C43.01 51.75 42.3 50.03 38.89 48.52ZM22.77 2.34C33.93 2.34 43.02 10.75 43.02 21.08C43.02 29.77 37.36 37.62 32.61 42.66C28.53 47 24.41 50.08 22.77 51.24C21.14 50.08 17.01 47 12.94 42.66C8.19 37.62 2.53 29.77 2.53 21.08C2.53 10.75 11.61 2.34 22.77 2.34ZM22.77 57.61C17.84 57.61 13.21 57.03 9.75 55.96C6.29 54.89 5.06 53.66 5.06 52.93C5.06 51.71 8.36 49.62 15.67 48.68C16.75 49.63 17.76 50.46 18.64 51.16C17.3 51.59 16.45 52.22 16.45 52.93C16.45 54.22 19.28 55.27 22.77 55.27C26.27 55.27 29.1 54.22 29.1 52.93C29.1 52.22 28.25 51.59 26.91 51.16C27.79 50.46 28.8 49.63 29.88 48.68C37.19 49.62 40.49 51.71 40.49 52.93C40.49 53.66 39.26 54.89 35.79 55.96Z" fill="#5F2DED" />
                <path d="M40.49 21.08C40.49 12.04 32.54 4.68 22.77 4.68C13.01 4.68 5.06 12.04 5.06 21.08C5.06 30.12 13.01 37.47 22.77 37.47C32.54 37.47 40.49 30.12 40.49 21.08ZM7.59 21.08C7.59 13.33 14.4 7.03 22.77 7.03C31.14 7.03 37.95 13.33 37.95 21.08C37.95 28.83 31.14 35.13 22.77 35.13C14.4 35.13 7.59 28.83 7.59 21.08Z" fill="#5F2DED" />
              </svg>
            </div>
            <div>
              <h3 className="text-2xl font-semibold text-blackColor dark:text-blackColor-dark leading-[38px]">Office address</h3>
              <p className="text-[13px] lg:text-[15px] text-contentColor dark:text-contentColor-dark leading-5">Auto Nagar, Karimnagar Road, Sircilla</p>
              <p className="text-[15px] text-contentColor dark:text-contentColor-dark leading-[26px]"></p>
            </div>
          </div>

          {/* Call Us */}
          <div className="pt-[15px] pr-[35px] pb-[25px] pl-5 lg:pt-10 lg:pb-10 lg:pl-[35px] transition-all duration-300 border border-borderColor dark:border-borderColor-dark shadow-address hover:shadow-address-hover hover:-translate-y-[5px] flex items-center gap-5 lg:gap-[30px]">
            <div>
              {/* Replace with a suitable phone icon or another SVG */}
              <svg width="50" height="50" viewBox="0 0 24 24" fill="none">
                <path d="M6.62 10.79a15.91 15.91 0 006.59 6.59l2.2-2.2a1 1 0 011.11-.21 12.26 12.26 0 004.28.82 1 1 0 011 1V20a1 1 0 01-1 1A16 16 0 013 5a1 1 0 011-1h3.5a1 1 0 011 1 12.26 12.26 0 00.82 4.28 1 1 0 01-.21 1.11l-2.49 2.4z" fill="#5F2DED"/>
              </svg>
            </div>
            <div>
              <h3 className="text-2xl font-semibold text-blackColor dark:text-blackColor-dark leading-[38px]">Call us</h3>
              <p className="text-[13px] lg:text-[15px] text-contentColor dark:text-contentColor-dark leading-5">+91 834 000 7081 </p>
              <p className="text-[15px] text-contentColor dark:text-contentColor-dark leading-[26px]">Mon – Sat, 9AM – 6PM</p>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
};

export default ContactPrimary;
