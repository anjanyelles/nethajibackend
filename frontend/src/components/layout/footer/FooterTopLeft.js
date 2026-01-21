import useIsSecondary from "@/hooks/useIsSecondary";
import Image from "next/image";
import React from "react";
import logoImage from "@/assets/images/logo/Nethaji Degree College - 01.png";
import Link from "next/link";
const FooterTopLeft = () => {
  const { isSecondary } = useIsSecondary();

  return (
    <div data-aos="fade-up">
      {isSecondary ? (
        <Link href="#">
          <Image src={logoImage} alt="" width={160}  height={100} />
        </Link>
      ) : (
        <>
       <h4 className="text-4xl md:text-size-25 lg:text-size-40 font-bold text-whiteColor leading-50px md:leading-10 lg:leading-16">
       Need a hand? We’re here to help!
</h4>
<p className="text-whiteColor text-opacity-65">
Reach out now and get your free quote — it's quick and hassle-free.
</p>

        </> 
      )}
    </div>
  );
};

export default FooterTopLeft;
