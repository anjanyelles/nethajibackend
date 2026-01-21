"use client";
import counter1 from "@/assets/images/counter/counter__1 copy.png";
import counter2 from "@/assets/images/counter/counter__2 copy.png";
import counter3 from "@/assets/images/counter/counter__3 copy.png";
import counter4 from "@/assets/images/counter/counter__4 copy.png";
import CounterItem from "@/components/shared/counter/CounterItem";
import counterUp from "@/libs/counterup";
import { useEffect } from "react";

const Counter2 = ({ type }) => {
  const items = [
    {
      name: "COURSES OFFERED",
      image: counter1,
      data: 6,
      symbol: "+",
    },
    {
      name: "EXPERT FACULTY",
      image: counter2,
      data: 25,
      symbol: "+",
    },
    {
      name: "YEARS OF EXPERIENCE",
      image: counter3,
      data: 10,
      symbol: "+",
    },
    {
      name: "SCHOLARSHIP PROGRAMS",
      image: counter4,
      data: 5,
      symbol: "+",
    },
  ];

  useEffect(() => {
    counterUp();
  }, []);

  return (
    <div>
      <div className={type === "lg" ? "container-fluid-2" : "container"}>
        <div className="counter flex flex-wrap pb-50px pt-20 md:pb-70px lg:pb-20 2xl:pb-100px gap-y-30px lg:gap-y-0">
          {items.map((item, idx) => (
            <CounterItem key={idx} item={item} />
          ))}
        </div>
      </div>
    </div>
  );
};

export default Counter2;