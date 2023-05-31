import {useState, useRef, useEffect, useMemo} from "react";

export default function useInfiniteScroll({
	root = null, target, threshold = 1, rootMargin = "0px", targetArray, endPoint = 1,
}) {
	const [count, setCount] = useState(0);
	const currentChild = useRef(null);

	const observer = useMemo(() => new IntersectionObserver(
		(entries, observerr) => {
			if (target?.current === null) {
				return;
			}
			if (entries[0].isIntersecting) {
				setCount(v => v + 1);
				observerr.disconnect();
			}
		},
	), [target, root, rootMargin, threshold]);

	useEffect(() => {
		if (target?.current === null) {
			return;
		}

		const observeChild = target.current.children[target.current.children.length - endPoint];

		if (observeChild && currentChild.current !== observeChild) {
			currentChild.current = observeChild;
			observer.observe(observeChild);
		}

		return () => {
			if (target.current !== null && observer) {
				observer.unobserve(target.current);
			}
		};
	}, [count, targetArray, target, endPoint]);

	return {
		count,
		setCount,
	};
}
