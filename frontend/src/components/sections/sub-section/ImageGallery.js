"use client";
 import { useEffect } from "react";
 
//  import img20250520_122600 from "@/assets/images/gallery/gallary_image/20250520_122600.jpg";
//  import img20250520_122612 from "@/assets/images/gallery/gallary_image/20250520_122612.jpg";
//  import img20250520_123050 from "@/assets/images/gallery/gallary_image/20250520_123050.jpg";
//  import img20250520_123055 from "@/assets/images/gallery/gallary_image/20250520_123055.jpg";
//  import img20250520_123059 from "@/assets/images/gallery/gallary_image/20250520_123059.jpg";
//  import img20250520_123104 from "@/assets/images/gallery/gallary_image/20250520_123104.jpg";
//  import img20250520_123108_1 from "@/assets/images/gallery/gallary_image/20250520_123108 (1).jpg";
 import img20250520_123108 from "@/assets/images/gallery/gallary_image/12.jpg";
 import img20250520_123114 from "@/assets/images/gallery/gallary_image/13.jpg";
 import img20250520_123118 from "@/assets/images/gallery/gallary_image/14.jpg";
 import img20250520_123122 from "@/assets/images/gallery/gallary_image/15.jpg";
//  import img20250815_085620 from "@/assets/images/gallery/gallary_image/20250815_085620.jpg";
 import img20250815_085650 from "@/assets/images/gallery/gallary_image/1.jpg";
 import img20250815_085936 from "@/assets/images/gallery/gallary_image/2.jpg";
 import img20250815_094046 from "@/assets/images/gallery/gallary_image/3.jpg";
 import img20250815_094052 from "@/assets/images/gallery/gallary_image/4.jpg";
 // import img20250904_155825 from "@/assets/images/gallery/gallary_image/20250904_155825.jpg";
 
 
 // import Untitled_1from "@/assets/images/gallery/gallary_image/20250904_161330.jpg";
 import img20250904_155825 from "@/assets/images/gallery/gallary_image/5.jpg";
 import img20250904_162154 from "@/assets/images/gallery/gallary_image/6.jpg";
 import img20250904_162952 from "@/assets/images/gallery/gallary_image/7.jpg";
 import img20250904_170705 from "@/assets/images/gallery/gallary_image/8.jpg";
 import img20250904_170858 from "@/assets/images/gallery/gallary_image/9.jpg";
 import img20250904_171444 from "@/assets/images/gallery/gallary_image/10.jpg";
 import imgWA0028 from "@/assets/images/gallery/gallary_image/11.jpg";     
 import imgWA0029 from "@/assets/images/gallery/gallary_image/12.jpg";
 
 import ImageSingle from "@/components/shared/sub-section/ImageSingle";
 import popup from "@/libs/popup";
 
 const GalleryPage = () => {
 useEffect(() => {
 popup();
 }, []);
 
 const images = [
 // img20250520_122600,
 
 // img20250815_085620,
 img20250815_085650,
 img20250815_085936,
 
 img20250815_094046, img20250904_171444,
 img20250815_094052,
 // img20250904_154541,
 img20250904_155825,
 
 
 // img20250904_161955,
 img20250904_162154,
 img20250904_162952,
 img20250904_170705,
 img20250904_170858,
 // img20250520_122612,
 // img20250520_123050,
 // img20250520_123055,
 // img20250520_123059,
 // img20250520_123104,
 // img20250520_123108_1,
 img20250520_123108,
 img20250520_123114,
 img20250520_123118,
 img20250520_123122,
 // img20250904_171444,
 imgWA0028,
 imgWA0029,
 ];
 
 return (
 <div className="container py-50px md:py-70px lg:py-100px">
 <div data-aos="fade-up" className="text-center mb-40px">
 <h2 className="text-3xl md:text-4xl font-bold text-blackColor dark:text-blackColor-dark">
 Campus Life & Events Gallery
 </h2>
 <p className="mt-3 text-lg text-contentColor dark:text-contentColor-dark max-w-3xl mx-auto">
 Explore our vibrant student life, cultural fests, AI workshops,
 classroom moments, and more at Nethaji Degree College.
 </p>
 </div>
 
 {/* Popup viewer */}
 <div className="gallary-container">
 <div className="popup">
 <div id="slider-container" className="slider-container">
 <span className="close-btn">&times;</span>
 <div className="slider-container-wrapper"></div>
 </div>
 <div className="slider-navigation">
 <button className="prev-btn"></button>
 <button className="next-btn"></button>
 </div>
 </div>
 
 {/* Grid Gallery */}
 <div className="grid grid-cols-2 md:grid-cols-4 gap-10px border border-borderColor2 dark:border-borderColor2-dark p-5 md:p-30px">
 {images.map((image, idx) => (
 <ImageSingle key={idx} image={image} />
 ))}
 </div>
 </div>
 </div>
 );
 };
 
 export default GalleryPage;
