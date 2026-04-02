"use client";
import EnrollmentForm from "@/components/shared/forms/EnrollmentForm";
import TabButtonPrimary from "@/components/shared/buttons/TabButtonPrimary";
import TabContentWrapper from "@/components/shared/wrappers/TabContentWrapper";
import Image from "next/image";
import shapImage2 from "@/assets/images/education/hero_shape2.png";
import shapImage3 from "@/assets/images/education/hero_shape3.png";
import shapImage4 from "@/assets/images/education/hero_shape4.png";
import shapImage5 from "@/assets/images/education/hero_shape5.png";
import useTab from "@/hooks/useTab";

const EnrollmentTabs = () => {
  const { currentIdx, handleTabClick } = useTab();
  
  const tabButtons = [
    { 
      name: "Enrollment Form", 
      content: <EnrollmentForm /> 
    },
    {
      name: "Google Form",
      content: (
        <div className="w-full">
          <div className="text-center mb-6">
            <h3 className="text-2xl font-bold text-blackColor dark:text-blackColor-dark mb-4">
              Alternative Enrollment
            </h3>
            <p className="text-gray-600 dark:text-gray-400">
              If you're facing any issues with the above form, please use our Google Form for enrollment submission.
            </p>
          </div>
          
          {/* Google Form Embed */}
          <div className="w-full" style={{ height: '800px', overflow: 'hidden' }}>
            <iframe
              src="https://docs.google.com/forms/d/e/1FAIpQLSfcEGuNu1rqI9v2IHK-oTrzs8yZv2pwgbFpTAQudKBcKbitkw/viewform?pli=1"
              width="100%"
              height="800"
              frameBorder="0"
              marginHeight="0"
              marginWidth="0"
              style={{ border: 'none', maxWidth: '100%', maxHeight: '800px' }}
              allowFullScreen
            >
              Loading...
            </iframe>
          </div>
          
          <div className="text-center mt-4">
            <p className="text-sm text-gray-500 dark:text-gray-400">
              Can't see the form? 
              <a 
                href="https://docs.google.com/forms/d/e/1FAIpQLSfcEGuNu1rqI9v2IHK-oTrzs8yZv2pwgbFpTAQudKBcKbitkw/viewform?pli=1"
                target="_blank"
                rel="noopener noreferrer"
                className="text-blue-600 hover:text-blue-700 underline ml-1"
              >
                Open in new tab
              </a>
            </p>
          </div>
        </div>
      )
    },
  ];

  return (
    <section className="relative">
      <div className="container py-100px">
        <div className="tab md:w-2/3 mx-auto">
          {/* tab controller */}
          <div className="tab-links grid grid-cols-2 gap-11px text-blackColor text-lg lg:text-size-22 font-semibold font-hind mb-43px mt-30px md:mt-0">
            {tabButtons?.map(({ name }, idx) => (
              <TabButtonPrimary
                key={idx}
                idx={idx}
                handleTabClick={handleTabClick}
                currentIdx={currentIdx}
                button={"lg"}
                name={name}
              />
            ))}
          </div>

          {/* tab contents */}
          <div className="shadow-container bg-whiteColor dark:bg-whiteColor-dark pt-10px px-5 pb-10 md:p-50px md:pt-30px rounded-5px">
            <div className="tab-contents">
              {tabButtons.map(({ content }, idx) => (
                <TabContentWrapper
                  key={idx}
                  isShow={idx === currentIdx || false}
                >
                  {content}
                </TabContentWrapper>
              ))}
            </div>
          </div>
        </div>
      </div>
      {/* animated icons */}
      <div>
        <Image
          loading="lazy"
          className="absolute right-[14%] top-[30%] animate-move-var"
          src={shapImage2}
          alt="Shape"
        />
        <Image
          loading="lazy"
          className="absolute left-[5%] top-1/2 animate-move-hor"
          src={shapImage3}
          alt="Shape"
        />
        <Image
          loading="lazy"
          className="absolute left-1/2 bottom-[60px] animate-spin-slow"
          src={shapImage4}
          alt="Shape"
        />
        <Image
          loading="lazy"
          className="absolute left-1/2 top-10 animate-spin-slow"
          src={shapImage5}
          alt="Shape"
        />
      </div>
    </section>
  );
};

export default EnrollmentTabs;
