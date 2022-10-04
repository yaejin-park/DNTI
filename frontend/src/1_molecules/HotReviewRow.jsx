import { Rating } from "@mui/material";
import { Link } from "react-router-dom";
import FavoriteBorderOutlinedIcon from "@mui/icons-material/FavoriteBorderOutlined";
import VisibilityOutlinedIcon from "@mui/icons-material/VisibilityOutlined";

export default function HotReviewRow({
  id,
  title,
  datetime,
  score,
  hit,
  nickname,
  likes,
}) {
  return (
    <Link to="/board/review/view" state={{ reviewId: id }}>
      <div className="flex flex-row h-8 w-full justify-between items-center p-5 bg-purple-200 hover:bg-purple-300 border-b-slate-200 border-b-2 box-border">
        <Rating value={score} size="small" readOnly />
        <div className="flex flex-row w-2/5 ml-5">
          <p className="text-ellipsis overflow-hidden font-bold">{title}</p>
        </div>
        <div className="flex flex-row w-3/5 justify-end items-center gap-2">
          <div>{nickname}</div>
          <div className="flex flex-row gap-1">
            <VisibilityOutlinedIcon fontSize="small" />
            <div className="w-[2.2rem]">{hit}</div>
          </div>
          <div className="flex flex-row gap-1">
            <FavoriteBorderOutlinedIcon fontSize="small" />
            <div className="w-[1.3rem]">{likes}</div>
          </div>
          <div>{datetime}</div>
        </div>
      </div>
    </Link>
  );
}
