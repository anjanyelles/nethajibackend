import Subject3 from "@/components/shared/popular-subjects/Subject3";

const PopularSubjects3 = ({ subject }) => {
  const subjects = [
    {
      title: "B.Sc (Data Science)",
      desc: "6 Courses",
    },
    {
      title: "B.Sc (Food Science)",
      desc: "4 Courses",
    },
    {
      title: "B.Com (Business Analytics)",
      desc: "5 Courses",
    },
    {
      title: "BBA (Artificial Intelligence)",
      desc: "5 Courses",
    },
    {
      title: "B.Sc (M.P.Cs)",
      desc: "7 Courses",
    },
    {
      title: "B.Com (CA)",
      desc: "6 Courses",
    },
  ];

  return (
    <section>
      <div className={subject ? "container-fluid-2" : "container"}>
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-30px pt-10 md:pt-0 pb-100px">
          {subjects.map((subject, idx) => (
            <Subject3 key={idx} subject={subject} />
          ))}
        </div>
      </div>
    </section>
  );
};

export default PopularSubjects3;
